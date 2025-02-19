package com.tbgram.domain.auth.filter;

import com.tbgram.domain.auth.exception.AuthException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.lang.model.type.ErrorType;
import java.io.IOException;
@Slf4j
public class SigninFilter implements Filter {

    private static final String[] POST_WHITE_LIST ={"/auth/signin","/members"};
    private static final String[] GET_WHITE_LIST = {"/news_feeds"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if ("POST".equals(method)) {
            if(!isPostWhiteList(requestURI)){
                HttpSession session = httpServletRequest.getSession(false);
                if(session == null || session.getAttribute("loginUser") == null){
                    throw new AuthException("로그인 되어있지 않습니다.");
                }
            }
        } else if ("GET".equals(method)) {
            if(!isGetWhiteList(requestURI)){
                HttpSession session = httpServletRequest.getSession(false);
                if(session == null || session.getAttribute("loginUser") == null){
                    throw new AuthException("로그인 되어있지 않습니다.");
                }
            }
        } else {
            HttpSession session = httpServletRequest.getSession(false);
            if(session == null || session.getAttribute("loginUser") == null){
                throw new AuthException("로그인 되어있지 않습니다.");
            }
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
    private boolean isPostWhiteList(String requestURI) {

        return PatternMatchUtils.simpleMatch(POST_WHITE_LIST,requestURI);
    }
    private boolean isGetWhiteList(String requestURI) {

        return PatternMatchUtils.simpleMatch(GET_WHITE_LIST,requestURI);
    }

}
