package com.blog.user.service;

import com.blog.user.database.Users;
import com.blog.user.dto.request.LoginRequestDto;
import com.blog.user.dto.response.TokenResponseDto;

public interface UserService {
  TokenResponseDto auth(LoginRequestDto dto) throws Exception;

  Users findCurrentUser() throws Exception;
}
