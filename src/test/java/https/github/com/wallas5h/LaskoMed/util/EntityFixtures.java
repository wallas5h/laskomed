package https.github.com.wallas5h.LaskoMed.util;

import https.github.com.wallas5h.LaskoMed.api.dto.AddressCreateRequestDto;
import https.github.com.wallas5h.LaskoMed.api.dto.PatientCreateRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.UserDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EntityFixtures {

  public static UserEntity someUserEntity(){

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(someRoleEntity());

    return  UserEntity.builder()
        .username("newTestowy")
        .email("test@mail.com")
        .password("Testpassword1")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }
  public static UserEntity someUserEntity2(){

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(someRoleEntity());


    return  UserEntity.builder()
        .username("john.doe")
        .email("john.doe@example.com")
        .password("test")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }
  public static PatientEntity somePatientEntity() {
    return PatientEntity.builder()
        .name("test")
        .surname("test")
        .pesel("12312312")
        .phone("555555555")
        .birthdate(LocalDate.parse("2000-01-01"))
        .email("test@test.com")
        .gender("male")
        .medicalPackage("standard")
        .address(someAddresEntity())
        .build();
  }
  public static AddressEntity someAddresEntity(){
    return AddressEntity.builder()
        .country("Poland")
        .voivodeship("Mazowieckie")
        .city("Warsaw")
        .street("Marymoncka")
        .houseNumber("52")
        .apartmentNumber("2")
        .postalCode("01-891")
        .build();
  }

  public static RoleEntity someRoleEntity(){
    return
        RoleEntity.builder()
        .name("PATIENT")
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
