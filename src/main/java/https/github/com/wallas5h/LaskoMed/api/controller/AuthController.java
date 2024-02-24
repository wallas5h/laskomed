package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(AuthController.AUTH)
@AllArgsConstructor
public class AuthController {
  public static final String AUTH = "/auth";
  public static final String LOGIN = "/login";
  public static final String REGISTER = "/register";

  private AuthService authService;


  @PostMapping(
      value = REGISTER,
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE
      })
  public ResponseEntity<Map<String, Object>> register(
      @RequestBody RegisterRequest request
  ){
    return authService.register(request);
  }
  @PostMapping(LOGIN)
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody LoginRequest request
  ){
    return authService.login(request);
  }
}
