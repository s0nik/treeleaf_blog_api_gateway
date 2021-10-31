package com.blog.user.config.response;

import com.blog.user.enums.ErrorCode;
import lombok.Data;

@Data
public class MyException extends Exception {

  private ErrorCode errorCode;

  private Object data;

  private String message;

  public MyException(ErrorCode errorCode, Object data) {
    this.errorCode = errorCode;
    this.data = data;
  }

  public MyException(ErrorCode errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

}
