package com.blog.user.service.impl;

import com.blog.user.config.security.OnlineUser;
import com.blog.user.database.Users;
import com.blog.user.dto.request.LoginRequestDto;
import com.blog.user.dto.response.TokenResponseDto;
import com.blog.user.enums.UserRole;
import com.blog.user.enums.UserStatus;
import com.blog.user.repository.UserRepository;
import com.blog.user.service.UserService;
import com.blog.user.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserRepository userRepository;


  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  TokenUtil tokenUtil;

  @Autowired
  UserDetailsService userDetailsService;

  @PostConstruct
  void createAdmin() {
    Users user = new Users();
    user.setId(1);
    user.setUserRole(UserRole.ROLE_ADMIN);
    user.setUsername("admin");
    user.setPassword(passwordEncoder.encode("String123"));
    user.setUserStatus(UserStatus.ACTIVE);
    userRepository.save(user);
  }

  @Override
  public TokenResponseDto auth(LoginRequestDto dto) throws Exception {
    authenticate(dto.getUsername(), dto.getPassword());
    TokenResponseDto response = new TokenResponseDto();
    final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
    response.setAuthToken(tokenUtil.generateToken(userDetails));
    return response;
  }

  public void authenticate(String username, String password) throws UsernameNotFoundException, AuthenticationException {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        username,
        password
    );
    final Authentication authentication = authenticationManager.authenticate(auth);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  @Override
  public Users findCurrentUser() throws Exception {
    try {
      return userRepository.findById(getId()).orElse(null);
    } catch (Exception ex) {
      return null;
    }
  }

  private Integer getId() throws Exception {
    OnlineUser user = (OnlineUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return user.getId();
  }

}
