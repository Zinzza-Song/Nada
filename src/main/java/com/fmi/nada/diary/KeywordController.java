package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

//    @GetMapping("")
    public void register(@RequestParam("keywords") KeywordDTO keywordDTO){
        Keyword keyword = new Keyword();
        String key = keywordDTO.getKeyword_name();
        String keywordArr[] = key.split(",");
        for(int i=0;i<keywordArr.length;i++){
            if(keywordArr[i]!=null &&  keyword.getKeyword_name().equals(keywordArr[i])) {
                keyword.setKeyword_cnt(keyword.getKeyword_cnt()+1);
                keywordService.register(keyword);
            }else {
            keyword.setKeyword_name(keywordArr[i]);
            keywordService.register(keyword);
        }
        }




    }

}
