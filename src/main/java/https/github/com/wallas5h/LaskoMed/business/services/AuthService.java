package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.api.mapper.AppUserMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer.RoleNames;
import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.EmailConfirmationTokenEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserRoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.EmailRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.EmailConfirmationTokenJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.RoleRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserRoleJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private EmailRepository emailRepository;
  private TokenService tokenService;
  private EmailConfirmationTokenJpaRepository emailConfirmationTokenJpaRepository;
  private MailService mailService;
  private EntityManager entityManager;
  private AppUserMapper appUserMapper;

  @Transactional
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

    ResponseEntity<Map<String, Object>> validationResult = requestUserDataValidation(request, response);
    if (validationResult != null) return validationResult;

    UserEntity userEntity = UserEntity.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .roles(roles)
        .confirmed(false)
        .enabled(true) //@TODO add enabled verification, f.e. email white list
        .build();

    UserEntity newUser;
    try {
      newUser = userDao.save(userEntity);
    } catch (Exception e) {
      response.put("error", "Invalid user data");
      return ResponseEntity.badRequest().body(response);
    }

    if (Objects.isNull(newUser)) {
      response.put("error", "Sorry, try later.");
      return ResponseEntity.internalServerError().body(response);
    }

    createUserRoleRecordAndSaveToDB(newUser, roleByName);

    sendEmailRegistrationConfirmation(newUser);

//    @TODO dodać endpoint confirm-registration

    response.put("message", "User registered successfully. Check your email and confirm registration");
    return ResponseEntity.ok().body(response);
  }

  private ResponseEntity<Map<String, Object>> requestUserDataValidation(RegisterRequest request, Map<String, Object> response) {
    if (userDao.findByUsername(request.getUsername()).isPresent()) {
      response.put("error", "Username already taken. Choose another one.");
      return ResponseEntity.badRequest().body(response);
    }
    if (userDao.findByEmail(request.getEmail()).isPresent()) {
      response.put("error", "A user with the provided email already exists. Add another one.");
      return ResponseEntity.badRequest().body(response);
    }

    if (StringUtils.isBlank(request.getPassword())) {
      response.put("error", "Invalid. Password cannot be blank");
      return ResponseEntity.badRequest().body(response);
    }
    return null;
  }

  private void createUserRoleRecordAndSaveToDB(UserEntity newUser, RoleEntity roleByName) {
    UserRoleEntity userRoleEntity = UserRoleEntity.builder()
        .appUserId(newUser.getUserId())
        .roleId(roleByName.getRoleId())
        .build();

    userRoleJpaRepository.save(userRoleEntity);
  }

  private void sendEmailRegistrationConfirmation(UserEntity newUser) {
    EmailConfirmationTokenEntity tokenEntity = EmailConfirmationTokenEntity.builder()
        .token(tokenService.generateToken())
        .user(newUser)
        .build();

    EmailConfirmationTokenEntity confirmationToken = emailConfirmationTokenJpaRepository.save(tokenEntity);

    mailService.sendConfirmationEmail(newUser, confirmationToken.getToken());
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
          "error", "Invalid credentials"
      ));
    }

    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.getUsernameOrEmail(),
          request.getPassword()
      ));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
          "error", "Invalid authentication"
      ));
    }

     if(!user.getConfirmed()){
       return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
           "error", "The user has not confirmed their registration. " +
               "Please check your email and confirm the registration."
       ));
     }


    String token = jwtTokenProvider.generateToken(user);

    return ResponseEntity.ok().body(Map.of(
        "message", "Login successful",
        "token", token
    ));
  }

  public ResponseEntity<Map<String, Object>> registerConfirmationByEmail(String token) {

    try{
      UserEntity user = emailRepository.findUserByToken(token);

      if(user.getConfirmed()){
        return ResponseEntity.ok().body(Map.of(
            "message", "The user has already been confirmed previously."
        ));
      }

      user.setConfirmed(true);
      userDao.save(user);

      return ResponseEntity.ok().body(Map.of(
          "message", "User confirmed"
      ));
    } catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
          "error", "Invalid authentication"
      ));
    }
  }
}
