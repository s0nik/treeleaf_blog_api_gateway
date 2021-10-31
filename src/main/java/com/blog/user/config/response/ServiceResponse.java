package com.blog.user.config.response;

import com.blog.user.enums.ErrorCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceResponse<T> {

  private Boolean status;

  private ErrorCode errorCode;

  private T data;

  private String message;

  public ServiceResponse(T data) {
    this.data = data;
    this.status = Boolean.TRUE;
  }

  public ServiceResponse(String message, Boolean status) {
    this.message = message;
    this.status = status;
  }


}
