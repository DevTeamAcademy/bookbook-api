package com.bookbook.security.service;

import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String MIKHALITSYN = "MIKHALITSYN";
  public static final String ROLE_USER = "ROLE_USER";
  public static final String ROLE_ADMIN = "ROLE_ADMIN";

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findOneByLoginOrEmail(loginOrEmail, loginOrEmail);
    return user.map(u -> {
      String role = MIKHALITSYN.equals(u.getLogin()) ? ROLE_ADMIN : ROLE_USER;
      Set<SimpleGrantedAuthority> authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority(role));
      return new Oauth2UserDetails(u.getLogin(), u.getPassword(), true, authorities);
    }).orElseThrow(() -> new UsernameNotFoundException("User not found :" + loginOrEmail));
  }

}