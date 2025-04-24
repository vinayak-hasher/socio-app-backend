//package com.example.final_assignment;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.nio.file.AccessDeniedException;
//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Slf4j
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
//        log.warn("Bad credentials: {}", ex.getMessage());
//        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password");
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
//        log.warn("Access denied: {}", ex.getMessage());
//        return buildErrorResponse(HttpStatus.FORBIDDEN, "You do not have permission to perform this action");
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
//        log.error("Runtime error: {}", ex.getMessage());
//        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
//        String path= request.getRequestURI();
//
//        if(path.startsWith("/swagger") || path.startsWith("/v3/api-docs")|| path.startsWith("/api-docs") || path.startsWith("/swagger-ui")){
//            log.debug("Bypassing Swagger ui");
//            return null;
//        }
//
//
//        log.error("Unexpected error: {}", ex.getMessage(), ex);
//        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
//    }
//
//    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
//        Map<String, Object> error = new LinkedHashMap<>();
//        error.put("timestamp", LocalDateTime.now());
//        error.put("status", status.value());
//        error.put("error", status.getReasonPhrase());
//        error.put("message", message);
//        return new ResponseEntity<>(error, status);
//    }
//}
