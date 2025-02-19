package com.tbgram.domain.common.interceptor;

import com.tbgram.domain.auth.dto.session.SessionUser;
import com.tbgram.domain.auth.exception.AuthException;
import com.tbgram.domain.common.annotation.CheckAuth;
import com.tbgram.domain.common.annotation.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class CheckAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod method) {
            CheckAuth checkAuth = method.getMethodAnnotation(CheckAuth.class);
            if (checkAuth != null) {
                HttpSession session = request.getSession(false);
                if (session == null) {
                    throw new AuthException("로그인 되어있지 않습니다.");
                }
                SessionUser sessionUser = (SessionUser) session.getAttribute("loginUser");
                if (sessionUser == null) {
                    throw new AuthException("로그인 되어있지 않습니다.");
                }
            }
        }
        return true;
    }
}
