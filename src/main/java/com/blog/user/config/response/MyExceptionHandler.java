package com.blog.user.config.response;

import com.blog.user.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
@Controller
public class MyExceptionHandler {

  ServiceResponse serviceResponse;

  private final static Logger LOGGER = Logger.getLogger(MyExceptionHandler.class.getName());


  @ResponseBody
  @ExceptionHandler
  public ResponseEntity<?> handleMyException(Exception exception) {
    HttpStatus status = HttpStatus.OK;
    serviceResponse = new ServiceResponse();

    if (exception instanceof MyException) {
      MyException ex = (MyException) exception;
      serviceResponse.setData(ex.getData());
      serviceResponse.setMessage(ex.getMessage());
      serviceResponse.setErrorCode(ex.getErrorCode());
    } else if (exception instanceof BadCredentialsException) {
      BadCredentialsException bce = (BadCredentialsException) exception;
      serviceResponse.setErrorCode(ErrorCode.U002);
      serviceResponse.setMessage("Invalid username or password");
    } else if (exception instanceof DisabledException) {
      serviceResponse.setErrorCode(ErrorCode.U001);
      serviceResponse.setMessage("User is disabled");
    } else if (exception instanceof AccessDeniedException) {
      status = HttpStatus.FORBIDDEN;
      serviceResponse.setErrorCode(ErrorCode.A403);
      serviceResponse.setMessage("Access denied");
    } else if (exception instanceof AuthenticationException) {
      status = HttpStatus.FORBIDDEN;
      serviceResponse.setErrorCode(ErrorCode.A403);
      serviceResponse.setMessage("Access denied");
    } else {
      serviceResponse.setErrorCode(ErrorCode.M001);
      serviceResponse.setMessage("Something went wrong");
      LOGGER.log(Level.INFO, exception.getMessage());
    }
    return new ResponseEntity<>(serviceResponse, status);
  }

}
