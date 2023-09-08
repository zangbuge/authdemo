package com.zangbuge.authdemo.security;

import cn.hutool.json.JSONUtil;
import com.zangbuge.authdemo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: lhm
 * @date: 2023/9/4
 */

@Slf4j
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        String uri = httpServletRequest.getRequestURI();
        log.info("权限不足,接口: {}", uri);
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSONUtil.toJsonStr(Result.createByError("权限不足")));
        } catch (Exception e1) {
            log.error("权限处理异常", e1);
        }
    }
}
