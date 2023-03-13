package com.fmi.nada.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class KeywordController {

    private final KeywordService keywordService;

    public void register(@RequestParam("keywords") KeywordDTO keywordDTO){
        Keyword keyword = new Keyword();
        String key = keywordDTO.getKeyword_name();
        String keywordArr[] = key.split(",");
        for(int i=0;i<keywordArr.length;i++){
            if(keywordArr[i]!=null &&  keyword.getKeyword_name().equals(keywordArr[i])) {

            }else {
            keyword.setKeyword_name(keywordArr[i]);
            keywordService.register(keyword);
        }
        }




    }

}
