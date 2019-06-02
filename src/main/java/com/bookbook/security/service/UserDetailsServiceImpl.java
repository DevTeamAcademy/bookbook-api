package com.bookbook.security.service;

import com.bookbook.user.domain.User;
import com.bookbook.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private static final String MIKHALITSYN = "MIKHALITSYN";
  private Oauth2UserDetails userMikhalitsyn;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostConstruct
  public void init() {
    HashSet<SimpleGrantedAuthority> authorities = new HashSet<SimpleGrantedAuthority>();
    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    userMikhalitsyn = new Oauth2UserDetails(MIKHALITSYN, passwordEncoder.encode(MIKHALITSYN), true, authorities);
  }

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    if (MIKHALITSYN.equals(loginId)) {
      return userMikhalitsyn;
    }

    Optional<User> user = userRepository.getUserDetails(loginId);
    return user.map(u -> {
      Set<SimpleGrantedAuthority> authorities = new HashSet<>();
      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
      return new Oauth2UserDetails(u.getUsername(), u.getPassword(), true, authorities);
    }).orElseThrow(() -> new UsernameNotFoundException("User not found :" + loginId));
  }

}