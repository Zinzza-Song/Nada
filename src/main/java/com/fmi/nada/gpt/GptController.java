package com.fmi.nada.gpt;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class GptController {

    private final ChatgptService chatgptService;

    @PostMapping("/send")
    @ResponseBody
    public ResponseModel send(HttpServletRequest req, @RequestBody String data) {
        String reqId = UUID.randomUUID().toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data);

        String message = element.getAsJsonObject().get("msg").getAsString();

        if(!StringUtils.hasText(message))
            return ResponseModel.fail("일기를 작성해 주세요");

        try {
            String resMsg = chatgptService.sendMessage(message);

            return ResponseModel.success(resMsg);
        } catch (Exception e) {
            return new ResponseModel(500, "error", e.getMessage());
        }

    }

}
