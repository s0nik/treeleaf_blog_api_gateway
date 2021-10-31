package com.blog.user.config.security;

import com.blog.user.database.Users;
import com.blog.user.enums.UserStatus;
import com.blog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
    return convertUserDetails(user);
  }

  private OnlineUser convertUserDetails(Users user) {
    OnlineUser user1 = new OnlineUser();
    user1.setUsername(user.getUsername());
    user1.setEnabled(user.getUserStatus() == UserStatus.ACTIVE);
    user1.setId(user.getId());
    user1.setPassword(user.getPassword());
    user1.setAuthorities(buildUserAuthority(user));
    return user1;
  }


  private List<GrantedAuthority> buildUserAuthority(Users userEntity) {
    Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
    grantedAuthoritySet.add(new SimpleGrantedAuthority(userEntity.getUserRole().toString()));
    return new ArrayList<>(grantedAuthoritySet);

  }

}
