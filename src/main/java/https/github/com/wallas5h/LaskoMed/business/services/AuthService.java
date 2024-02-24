package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer.RoleNames;
import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserRoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.RoleRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserRoleJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {


  private UserDao userDao;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;
  private JwtTokenProvider jwtTokenProvider;

  private RoleRepository roleRepository;
  private UserRoleJpaRepository userRoleJpaRepository;
  private EntityManager entityManager;

  public ResponseEntity<Map<String, Object>> register(RegisterRequest request) {
    Set<RoleEntity> roles = new HashSet<>();
    Map<String, Object> response = new HashMap<>();
    RoleEntity roleByName;

    if (isValidRole(request.getRole())) {
      roleByName = roleRepository.findByName(request.getRole());
      roles.add(roleByName);
    } else {
      response.put("error", "Invalid role specified");
      return ResponseEntity.badRequest().body(response);
    }

    if (userDao.findByUsername(request.getUsername()).isPresent()) {
      response.put("error", "Username already taken. Choose another one.");
      return ResponseEntity.badRequest().body(response);
    }
    if (userDao.findByEmail(request.getEmail()).isPresent()) {
      response.put("error", "A user with the provided email already exists. Add another one.");
      return ResponseEntity.badRequest().body(response);
    }

    UserEntity userEntity = UserEntity.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(roles)
        .confirmed(request.getConfirmed())
        .enabled(request.getEnabled())
        .build();

    UserEntity newUser = userDao.save(userEntity);

    if (Objects.isNull(newUser)) {
      response.put("error", "Sorry, try later.");
      return ResponseEntity.badRequest().body(response);
    }

    UserRoleEntity userRoleEntity = UserRoleEntity.builder()
        .appUserId(newUser.getUserId())
        .roleId(roleByName.getRoleId())
        .build();

    userRoleJpaRepository.save(userRoleEntity);

    response.put("response", "User added succesfully");
    return ResponseEntity.ok().body(response);
  }

  private boolean isValidRole(String role) {
    for (RoleNames validRole : RoleNames.values()) {
      if (validRole.name().equals(role)) {
        return true;
      }
    }
    return false;
  }

  public ResponseEntity<Map<String, Object>> login(LoginRequest request) {
    UserEntity user = null;
    try {
      user = userDao.findByUsernameOrEmail(request.getUsernameOrEmail());
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
          "error", e.getMessage()
      ));
    }

    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.getUsernameOrEmail(),
          request.getPassword()
      ));
    } catch (BadCredentialsException e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
          "error", e.getMessage()
      ));
    }


    String token = jwtTokenProvider.generateToken(user);

    return ResponseEntity.ok().body(Map.of(
        "response", "Login successful",
        "token", token
    ));
  }
}
