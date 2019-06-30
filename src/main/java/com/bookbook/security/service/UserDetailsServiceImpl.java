package com.bookbook.security.service;

import com.bookbook.user.domain.User;
import com.bookbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String MIKHALITSYN = "MIKHALITSYN";
  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
    User user = userService.findOneByLoginOrEmail(loginOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found :" + loginOrEmail));
    return createUserDetails(user);
  }

  public UserDetails loadUserByGuid(String guid) throws UsernameNotFoundException {
    User user = userService.getExistent(guid);
    return createUserDetails(user);
  }

  private UserDetails createUserDetails(User user) {
    String role = MIKHALITSYN.equals(user.getLogin()) ? ROLE_ADMIN : ROLE_USER;
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    authorities.add(new SimpleGrantedAuthority(role));
    return new Oauth2UserDetails(user.getLogin(), user.getPassword(), user.getGuid(), authorities);
  }

}