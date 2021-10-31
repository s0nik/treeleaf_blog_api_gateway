package com.blog.user.database;

import com.blog.user.enums.UserRole;
import com.blog.user.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Data
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Basic(optional = false)
  @Column(insertable = false, updatable = false, nullable = false)
  private int id;

  @Size(max = 100)
  @Email
  @Column(name = "email")
  private String email;

  @Column(name = "password", nullable = false)
  @JsonIgnore
  private String password;

  @Size(max = 200)
  @Column(name = "username")
  private String username;

  @Column(name = "user_role")
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  @Column(name = "user_status")
  @Enumerated(EnumType.STRING)
  private UserStatus userStatus;

}
