package com.blog.user.controller;

import com.blog.user.config.response.MyException;
import com.blog.user.config.response.ServiceResponse;
import com.blog.user.dto.request.PostApiRequestDto;
import com.blog.user.dto.request.PostRequestDto;
import com.blog.user.dto.response.PostResponseDto;
import com.blog.user.enums.ErrorCode;
import com.blog.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("blog")
public class BlogController {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  UserService userService;

  @PostMapping("post")
  @Secured("ROLE_ADMIN")
  public ServiceResponse postBlog(@RequestBody @Valid PostRequestDto dto, BindingResult result) throws Exception {
    if (result.hasErrors()) {
      Map<String, String> errors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
      throw new MyException(ErrorCode.V001, errors);
    }
    ServiceResponse response = new ServiceResponse();
    PostApiRequestDto requestDto = new PostApiRequestDto(dto);
    requestDto.setUser(userService.findCurrentUser().getId());
    final ServiceResponse serviceResponse = restTemplate.postForObject("http://localhost:8001/post", requestDto, ServiceResponse.class);
    response.setData(serviceResponse.getData());
    response.setMessage(serviceResponse.getMessage());
    response.setErrorCode(serviceResponse.getErrorCode());
    return response;
  }

  @GetMapping("post")
  public ServiceResponse getPosts(Pageable pg) throws Exception {
    ServiceResponse response = new ServiceResponse();
    final ServiceResponse serviceResponse = restTemplate.getForObject("http://localhost:8001/post?page={page}&size={size}", ServiceResponse.class, pg.getPageNumber(), pg.getPageSize());
    response.setData(serviceResponse.getData());
    response.setMessage(serviceResponse.getMessage());
    response.setErrorCode(serviceResponse.getErrorCode());
    response.setStatus(serviceResponse.getStatus());
    return response;
  }

  @PutMapping("{id}")
  public ServiceResponse<PostResponseDto> getPostById(@PathVariable Integer id, @RequestBody @Valid PostRequestDto dto, BindingResult result) throws Exception {
    if (result.hasErrors()) {
      Map<String, String> errors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
      throw new MyException(ErrorCode.V001, errors);
    }
    PostApiRequestDto requestDto = new PostApiRequestDto(dto);
    requestDto.setUser(userService.findCurrentUser().getId());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<PostApiRequestDto> entity = new HttpEntity<PostApiRequestDto>(requestDto, headers);
    final ResponseEntity<ServiceResponse> responseEntity = restTemplate.exchange("http://localhost:8001/post/{id}", HttpMethod.PUT, entity, ServiceResponse.class, id);
    return responseEntity.getBody();
  }

  @DeleteMapping("{id}")
  public ServiceResponse deletePost(@PathVariable Integer id) throws Exception {
    ServiceResponse response = new ServiceResponse();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ServiceResponse> entity = new HttpEntity<ServiceResponse>(headers);
    final ResponseEntity<ServiceResponse> exchange = restTemplate.exchange("http://localhost:8001/post/{id}", HttpMethod.DELETE, entity, ServiceResponse.class, id);
    return exchange.getBody();
  }

}
