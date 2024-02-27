package https.github.com.wallas5h.LaskoMed.util;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoFixtures {

  private String name;
  private String surname;
  private String pesel;
  private String birthdate;
  private String email;
  private String phone;
  private String gender;
  private String medicalPackage;
  private AddressCreateRequestDto address;
  private String city;
  private String street;
  private String houseNumber;
  private String apartmentNumber;
  private String postalCode;

  public static RegisterRequest someUserRegisterRequest() {
    return RegisterRequest.builder()
        .username("testowy")
        .email("test@mail.com")
        .password("testpassword")
        .role("PATIENT")
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static LoginRequest someUserLoginRequest() {
    return LoginRequest.builder()
        .usernameOrEmail("testowy")
        .password("testpassword")
        .build();
  }

  public static DoctorCreateRequest someDoctorCreateRequest() {
    return DoctorCreateRequest.builder()
        .name("test")
        .surname("test")
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
