package com.blog.user.dto.request;

import lombok.Data;

@Data
public class PostApiRequestDto extends PostRequestDto {

  private Integer user;

  public PostApiRequestDto(PostRequestDto dto) {
    super.setTitle(dto.getTitle());
    super.setContent(dto.getContent());
  }

}
