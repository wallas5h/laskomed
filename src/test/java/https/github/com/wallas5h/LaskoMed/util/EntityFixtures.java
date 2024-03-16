package https.github.com.wallas5h.LaskoMed.util;

import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class EntityFixtures {

  public static UserEntity someUserEntity() {

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(somePatientRoleEntity());

    return UserEntity.builder()
        .username("newTestowy")
        .email("test@mail.com")
        .password("Testpassword1")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static UserEntity someUserPatientEntity() {

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(somePatientRoleEntity());


    return UserEntity.builder()
        .username("john.doe")
        .email("john.doe@example.com")
        .password("test")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static UserEntity someUserDoctorEntity() {

    Set<RoleEntity> roles = new HashSet<>();
    roles.add(someDoctorRoleEntity());


    return UserEntity.builder()
        .username("jan.mocny")
        .email("jan.mocny@example.com")
        .password("test")
        .roles(roles)
        .confirmed(true)
        .enabled(true)
        .build();
  }

  public static DoctorEntity someDoctorEntity() {
    return DoctorEntity.builder()
        .name("test")
        .pesel("test")
        .phone("test")
        .appUser(someUserEntity())
        .surname("test")
        .pwzNumber("test")
        .specialization("test")
        .build();
  }

  public static DoctorEntity someDoctorEntity2() {
    return DoctorEntity.builder()
        .appUser(someUserDoctorEntity())
        .name("jan")
        .surname("mocny")
        .pesel("12312312")
        .specialization("test")
        .pwzNumber("12345")
        .phone("555555555")
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

  public static AddressEntity someAddresEntity() {
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

  public static ClinicEntity someClinicEntity() {
    return ClinicEntity.builder()
        .name("Clinic")
        .regon("123456")
        .nip("123456")
        .address(someAddresEntity())
        .build();
  }

  public static AvailableAppointmentEntity someAvailableAppointmentEntity() {
    return AvailableAppointmentEntity.builder()
        .dateAvailable(LocalDate.now().plusDays(2))
        .startTime(LocalTime.of(8, 0, 0))
        .endTime(LocalTime.of(8, 20, 0))
        .isActive(true)
        .build();
  }

  public static RoleEntity somePatientRoleEntity() {
    return
        RoleEntity.builder()
            .name("PATIENT")
            .build();
  }

  public static RoleEntity someDoctorRoleEntity() {
    return
        RoleEntity.builder()
            .name("DOCTOR")
            .build();
  }

  public static RegisterRequest someUserRequest() {
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
