package https.github.com.wallas5h.LaskoMed.util;

import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.UserDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EntityFixtures {

  public static UserEntity someUserEntity(){

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(
        RoleEntity.builder()
        .name("PATIENT")
        .build()
    );

    return  UserEntity.builder()
        .username("testowy")
        .email("test@mail.com")
        .password("testpassword")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static RegisterRequest someUserRequest(){
    return RegisterRequest.builder()
        .username("testowy")
        .email("test@mail.com")
        .password("testpassword")
        .role("PATIENT")
        .confirmed(true)
        .enabled(true)
        .build();
  }

}
