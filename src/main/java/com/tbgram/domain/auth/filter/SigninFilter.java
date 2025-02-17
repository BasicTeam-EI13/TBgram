package com.tbgram.domain.auth.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

public class SigninFilter implements Filter {

//    private static final String[] WHITE_LIST ={"/auth/signin","/members"};
    private static final String[] WHITE_LIST ={"/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if(!isWhiteList(requestURI)){
            HttpSession session = httpServletRequest.getSession(false);


            if(session == null || session.getAttribute("user") == null){
                throw new RuntimeException("로그인 되어있지 않습니다.");
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
    private boolean isWhiteList(String requestURI) {

        return PatternMatchUtils.simpleMatch(WHITE_LIST,requestURI);
    }

}
