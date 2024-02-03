package https.github.com.wallas5h.LaskoMed.domain;

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
    Patient patient;
    Clinic clinic;

}
