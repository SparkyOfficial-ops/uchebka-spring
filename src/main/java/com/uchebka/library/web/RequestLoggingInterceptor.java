package com.uchebka.library.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Точка внутри Spring MVC, уже после фильтра безопасности и до метода контроллера.
 * {@code true} пропускает запрос дальше. {@code false} оборвал бы цепочку.
 */
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Гонец доскакал до избы MVC: {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
}
