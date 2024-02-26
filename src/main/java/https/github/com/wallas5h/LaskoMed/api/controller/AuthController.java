package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "User", description = "Methods for user management")
@RestController
@RequestMapping(AuthController.AUTH)
@AllArgsConstructor
public class AuthController {
  public static final String AUTH = "/auth";
  public static final String LOGIN = "/login";
  public static final String REGISTER = "/register";

  private AuthService authService;

  @Operation(summary = "Create new user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "New user created",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
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

  @Operation(summary = "Login patient")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User logged",
          content = @Content),
      @ApiResponse(responseCode = "400", description = "Invalid input data",
          content = @Content),
      @ApiResponse(responseCode = "401", description = "Unauthorised access",
          content = @Content)
  })
  @PostMapping(LOGIN)
  public ResponseEntity<Map<String, Object>> login(
      @RequestBody LoginRequest request
  ){
    return authService.login(request);
  }
}
