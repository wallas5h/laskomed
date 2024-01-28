package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "addressId")
@ToString(exclude = "patient")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name="country")
    private String country;

    @Column(name="vivodeship")
    private String vivodeship;

    @Column(name="postal_code")
    private String postalCode;

    @Column(name="house_number")
    private String houseNumber;

    @Column(name="region")
    private String region;

    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @Column(name="apartment_number")
    private String apartmentNumber;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private PatientEntity patient;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
    private ClinicEntity clinic;
}
