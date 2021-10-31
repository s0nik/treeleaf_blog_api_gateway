package com.blog.user.config.security;

import com.blog.user.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthFilter extends OncePerRequestFilter {

  @Value("${app.header}")
  private String tokenHeader;

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenUtil tokenUtil;

  @Autowired
  private HttpServletRequest request;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String authToken = request.getHeader(tokenHeader);
    try {
      if (!validToken(authToken)) {
        SecurityContextHolder.getContext().setAuthentication(null);
      }
    } catch (Exception ex) {
      Logger.getLogger(AuthFilter.class.getName()).log(Level.SEVERE, null, ex);
    }
    chain.doFilter(request, response);
  }

  private boolean validToken(String authToken) throws Exception {
    if (authToken != null && authToken.length() > 0) {
      authToken = authToken.replace("Bearer ", "");
      String username = tokenUtil.getUsernameFromToken(authToken);
      if (username != null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (tokenUtil.validateToken(authToken, userDetails)) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
          return true;
        }
      }
    }
    return false;
  }
}
