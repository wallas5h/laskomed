package https.github.com.wallas5h.LaskoMed.util;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import lombok.experimental.UtilityClass;

import java.util.HashSet;

@UtilityClass
public class DtoFixtures {


  public static RegisterRequest someUserRegisterRequest() {
    return RegisterRequest.builder()
        .username("newTestowy")
        .email("newtest@mail.com")
        .password("Testpassword1")
        .role(EnumsContainer.RoleNames.PATIENT.name())
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static LoginRequest someUserLoginRequest() {
    return LoginRequest.builder()
        .usernameOrEmail("newTestowy")
        .password("Testpassword1")
        .build();
  }
  public static LoginRequest doctorLoginRequest() {
    return LoginRequest.builder()
        .usernameOrEmail("adam.mocny")
        .password("test")
        .build();
  }
  public static LoginRequest patientLoginRequest() {
    return LoginRequest.builder()
        .usernameOrEmail("john.doe")
        .password("test")
        .build();
  }


  public static UserDTO someUserDto() {
    HashSet<RoleDTO> roles = new HashSet<>();

    roles.add(RoleDTO.builder().roleId(1L).name(EnumsContainer.RoleNames.PATIENT.name()).build());

    return UserDTO.builder()
        .username("newTestowy")
        .email("newtest@mail.com")
        .confirmed(true)
        .enabled(true)
        .roles(roles)
        .build();
  }


  public static DoctorCreateRequest someDoctorCreateRequest() {
    return DoctorCreateRequest.builder()
        .name("jan")
        .surname("mocny")
        .pesel("12312312")
        .specialization("test")
        .PwzNumber("12345")
        .phone("555555555")
        .build();
  }

  public static PatientCreateRequest somePatientCreateRequest() {
    return PatientCreateRequest.builder()
        .name("test")
        .surname("test")
        .pesel("12312312")
        .phone("555555555")
        .birthdate("2000-01-01")
        .email("test@test.com")
        .gender("male")
        .medicalPackage("standard")
        .address(someAddressCreateRequest())
        .build();
  }

  public static AddressCreateRequestDto someAddressCreateRequest() {
    return AddressCreateRequestDto.builder()
        .city("Warsaw")
        .street("Marymoncka")
        .houseNumber("52")
        .apartmentNumber("2")
        .postalCode("01-891")
        .build();
  }


}
