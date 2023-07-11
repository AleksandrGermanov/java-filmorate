package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Component
@Slf4j
public class ApplicationExceptionResolver extends AbstractHandlerExceptionResolver {
    @Override
    protected ModelAndView doResolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        try {
            return handleUnknownException(ex, request, response);
        } catch (Exception e) {
            log.warn(e.getClass() + " has occurred during ExceptionHandling!");
        }
        return null;
    }

    private ModelAndView
    handleUnknownException(Exception ex, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.warn(ex.getClass() + " has occurred with stack trace of " + Arrays.toString(ex.getStackTrace()));
        response.setStatus(500);
        response.getOutputStream().write("Unknown Server Error.".getBytes(StandardCharsets.UTF_8));
        return new ModelAndView();
    }
}

