package com.account_service.interceptor;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor implements HandlerInterceptor {

@Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    MDC.put("api", request.getRequestURI());
    MDC.put("method", request.getMethod());
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }
 @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    MDC.clear();
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
