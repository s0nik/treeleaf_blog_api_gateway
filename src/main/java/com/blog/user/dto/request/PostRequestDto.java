package com.blog.user.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PostRequestDto {

  @NotEmpty(message = "Title cannot be empty")
  private String title;

  @NotEmpty(message = "Content cannot be empty")
  private String content;

}
