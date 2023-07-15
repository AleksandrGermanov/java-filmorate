package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> errorResponseBody(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        message.append("ConstraintViolationException has occurred: ");
        e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath().toString()
                        + ": " + violation.getMessage() + " ")
                .forEach(message::append);
        String formedMessage = message.toString();
        log.warn(formedMessage);
        return new ResponseEntity<>('\"' + formedMessage + '\"', HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> errorResponseBody(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        message.append("MethodArgumentNotValidException has occurred: ");
        e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getObjectName() + " " + error.getField()
                        + ": " + error.getDefaultMessage() + " ")
                .forEach(message::append);
        String formedMessage = message.toString();
        log.warn(formedMessage);
        return new ResponseEntity<>('\"' + formedMessage + '\"', HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorResponseBody(Exception e) {
        String message = "Unknown Exception has occurred: " + e.getClass()
                + " with message \'" + e.getMessage() + "\'.";
        log.warn(message + "StackTrace: " + e.getStackTrace());
        return new ResponseEntity<>('\"' + message + '\"', HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
