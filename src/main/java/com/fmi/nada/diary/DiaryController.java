    package com.fmi.nada.diary;

    import com.fmi.nada.user.BlockListDto;
    import com.fmi.nada.user.Member;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.web.PageableDefault;
    import org.springframework.http.MediaType;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.web.bind.annotation.*;

    import javax.servlet.http.Cookie;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import javax.validation.Valid;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.LocalTime;
    import java.time.ZoneOffset;
    import java.util.List;

    /**
     * Diary Controller
     */
    @Controller
    @RequiredArgsConstructor
    @RequestMapping("/diary")
    public class DiaryController {

        private final DiaryService diaryService;

        private final KeywordService keywordService;

        private final CommentService commentService;

        private final AnalyzedService analyzedService;
        //좋아요를 위한 Like 가져와야 함.

        //다이어리 게시판 페이지
        @GetMapping
        public String DiaryMain(@PageableDefault Pageable pageable,
                                @RequestParam(value = "type", required = false) String type,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                Model model) {

            Page<Diary> allDiaryList = null;

            if (keyword == null) {
                allDiaryList = diaryService.getDiaryList(pageable);
            } else if (type.equals("content")) {
                allDiaryList = diaryService.findAllByDiaryContentContaining(keyword, pageable);
            } else if (type.equals("writer")) {
                allDiaryList = diaryService.findAllByDiaryWriterContaining(keyword, pageable);
            } else if (type.equals("keyword")) {
                allDiaryList = diaryService.findAllByDiaryKeywordsContaining(keyword, pageable);
            }

            model.addAttribute("allDiaryList", allDiaryList);
            model.addAttribute("type", type);
            model.addAttribute("keyword", keyword);
            return "diary/index";
        }

        // 다이어리 상세 페이지
        @GetMapping("read/{diaryIdx}")
        public String readDiary(@PathVariable("diaryIdx") Long diaryIdx,
                                @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication,
                                Model model,
                                @ModelAttribute("blockListDto") BlockListDto blockListDto) {
            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("member", member);

            Diary diary = diaryService.getDiaryDetail(diaryIdx);
            viewCountValidation(diary, request, response);
            model.addAttribute("readDiaryBean", diary);

            List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
            model.addAttribute("commentList", commentList);

            return "diary/read";
        }

        // 다이어리 작성 페이지
        @GetMapping("/write")
        public String DiaryWrite(@ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO, Authentication authentication, Model model) {

            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("member", member);
            diaryDTO.setDiaryWriter(member.getMemberNickname());

            return "diary/write";
        }

        @PostMapping("write_pro")
        public String DiaryWrite_pro(
                @Valid @ModelAttribute("writeDiaryBean") DiaryDTO diaryDTO,
                BindingResult bindingResult,
                Authentication authentication) {
            if (bindingResult.hasErrors()) {
                for (int i = 0; i < bindingResult.getAllErrors().size(); ++i)
                    System.out.println(bindingResult.getAllErrors().get(i));
                return "diary/write";
            }

            Member member = (Member) authentication.getPrincipal();

            Diary diary = diaryService.registerDiary(
                    member.getMemberIdx(),
                    diaryDTO.getDiarySubject(),
                    diaryDTO.getDiaryWriter(),
                    diaryDTO.getDiaryContent(),
                    diaryDTO.getDiaryKeywords(),
                    diaryDTO.getDiaryAnalyze(),
                    diaryDTO.getDiaryPublicable(),
                    diaryDTO.getDiaryAnalyzePublicable());

            analyzedService.resisterAnalyze(diary, diary.getDiaryAnalyze(), Integer.parseInt(diaryDTO.getAnalyzeScore()));

            insertKeywords(diaryDTO);

            return "redirect:/diary/read/" + diary.getDiaryIdx();
        }


        // 다이어리 공감에 대한 ajax
        @GetMapping("sympathy/{diaryIdx}")
        @ResponseBody
        public void sympathyAjax(@PathVariable("diaryIdx") Long diaryIdx) {

        }

        // 댓글 좋아요에 대한 ajax
        @GetMapping("comment_like/{diaryIdx}")
        @ResponseBody
        public void likeAjax(@PathVariable("diaryIdx") Long diaryIdx) {

        }

        @RequestMapping(value = "/comment_write", method = RequestMethod.POST)
        public String commentWrite(@RequestParam("diaryIdx") Long diaryIdx,
                                          @RequestParam("commentInput") String commentInput,
                                          Authentication authentication,
                                          Model model) {

            Member member = (Member) authentication.getPrincipal();
            Comment comment = new Comment(member.getMemberIdx(),
                    diaryIdx,
                    commentInput,
                    member.getMemberNickname(),
                    member.getUsername()
            );
            comment.setCommentLikeCnt(0);

            commentService.resisterComment(comment);

            List<Comment> commentList = commentService.findAllByDiaryIdxOrderByCommentDateDesc(diaryIdx);
            model.addAttribute("commentList", commentList);

            return "/diary/read :: tbody";
        }

        // 다이어리 수정 페이지
        @GetMapping("modify/{diaryIdx}")
        public String modifyDiary(@PathVariable("diaryIdx") Long diaryIdx, @RequestParam("pageCnt") int pageCnt, @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO, Authentication authentication, Model model) {

            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("member", member);

            return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;
        }


        // 다이어리 수정 로직
        @PutMapping("modify_pro/{diaryIdx}")
        public String modifyDiary_pro(@PathVariable("diaryIdx") Long diaryIdx, @RequestParam("pageCnt") int pageCnt, @Valid @ModelAttribute("diaryModifyBean") DiaryDTO diaryDTO, BindingResult bindingResult, Authentication authentication, Model model) {
            if (bindingResult.hasErrors()) return "diary/modify/" + diaryIdx + "?pageCnt=" + pageCnt;

            Member member = (Member) authentication.getPrincipal();
            model.addAttribute("member", member);

            Diary diary = diaryService.findByDiaryIdx(diaryIdx);
            diary.setDiarySubject(diaryDTO.getDiarySubject());
            diary.setDiaryContent(diaryDTO.getDiaryContent());
            diary.setDiaryKeywords(diaryDTO.getDiaryKeywords());
            diary.setDiaryAnalyze(diaryDTO.getDiaryAnalyze());
            diary.setDiaryPublicable(diaryDTO.getDiaryPublicable());
            diary.setDiaryAnalyzePublicable(diaryDTO.getDiaryAnalyzePublicable());

            insertKeywords(diaryDTO);
            diaryService.modifyDiary(diary);

            return "redirect:/read/" + diary.getDiaryIdx() + "?pageCnt=" + pageCnt;
        }

        // 다이어리 삭제 로직
        @DeleteMapping("delete/{diaryIdx}")
        public String deleteDiary(@PathVariable("diaryIdx") Long diaryIdx) {
            diaryService.deleteDiary(diaryIdx);
            return "redirect:index?pageNum=" + 1;
        }

        private void insertKeywords(DiaryDTO diaryDTO) {
            String keywordArr[] = diaryDTO.getDiaryKeywords().split(",");
            for (int i = 0; i < keywordArr.length; i++) {
                if (keywordArr[i] != null) {
                    Keyword keyword = keywordService.findByKeywordName(keywordArr[i]);

                    if (keyword != null) {
                        Integer a = keyword.getKeywordCnt();
                        System.out.println(a);
                        Integer b = a + 1;
                        keyword.setKeywordCnt(b);
                        keywordService.register(keyword);
                    } else {
                        keywordService.register(new Keyword(keywordArr[i]));
                    }
                }
            }
        }

        private void viewCountValidation(Diary diary, HttpServletRequest request, HttpServletResponse response) {
            Cookie[] cookies = request.getCookies();
            Cookie cookie = null;
            boolean isCookie = false;
            // request에 쿠키들이 있을 때
            for (int i = 0; cookies != null && i < cookies.length; i++) {
                // postView 쿠키가 있을 때
                if (cookies[i].getName().equals("noticeView")) {
                    // cookie 변수에 저장
                    cookie = cookies[i];
                    // 만약 cookie 값에 현재 게시글 번호가 없을 때
                    if (!cookie.getValue().contains("[" + diary.getDiaryIdx() + "]")) {
                        // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                        diary.addViewCount();
                        cookie.setValue(cookie.getValue() + "[" + diary.getDiaryIdx() + "]");
                    }
                    isCookie = true;
                    break;
                }
            }
            // 만약 noticeView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
            if (!isCookie) {
                diary.addViewCount();
                cookie = new Cookie("noticeView", "[" + diary.getDiaryIdx() + "]"); // oldCookie에 새 쿠키 생성
            }

            // 쿠키 유지시간을 오늘 하루 자정까지로 설정
            long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
            long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
            cookie.setPath("/"); // 모든 경로에서 접근 가능
            cookie.setMaxAge((int) (todayEndSecond - currentSecond));
            response.addCookie(cookie);
        }
    }
