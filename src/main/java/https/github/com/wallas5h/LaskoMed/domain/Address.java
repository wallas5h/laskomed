package https.github.com.wallas5h.LaskoMed.domain;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "postalCode"})
public class Address {

    Long addressId;
    String country;
    String voivodeship;
    String postalCode;
    String houseNumber;
    String region;
    String city;
    String street;
    String apartmentNumber;
    PatientEntity patient;
    ClinicEntity clinic;







}
