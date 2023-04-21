package com.fmi.nada.error;

import org.apache.http.HttpStatus;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class WebErrorController implements ErrorController {

    public String getErrorPath() {
        return null;
    }

    @GetMapping("/error")
    public String errorHandler(HttpServletRequest req) {
        Object status = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.SC_NOT_FOUND)
                return "error/404error";
            else
                return "error/error";
        }

        return "error/error";
    }

}

