package com.fischer.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fischer.assistant.ResultType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResultType resultType=new ResultType(403,null,"暂无权限访问");
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(resultType);
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(s);
    }
}
