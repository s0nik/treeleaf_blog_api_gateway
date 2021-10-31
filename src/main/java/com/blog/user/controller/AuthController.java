package com.blog.user.controller;

import com.blog.user.config.response.MyException;
import com.blog.user.config.response.ServiceResponse;
import com.blog.user.dto.request.LoginRequestDto;
import com.blog.user.dto.response.TokenResponseDto;
import com.blog.user.enums.ErrorCode;
import com.blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class AuthController {

  @Autowired
  UserService userService;

  @PostMapping("auth")
  public ServiceResponse<TokenResponseDto> auth(@RequestBody @Valid LoginRequestDto dto, BindingResult result) throws Exception {
    if (result.hasErrors()) {
      Map<String, String> errors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
      throw new MyException(ErrorCode.V001, errors);
    }
    ServiceResponse<TokenResponseDto> response = new ServiceResponse<>();
    response.setData(userService.auth(dto));
    return response;
  }


  @GetMapping("/user")
  @Secured("ROLE_ADMIN")
  public ServiceResponse test() {
    ServiceResponse response = new ServiceResponse();
    return response;
  }

}
