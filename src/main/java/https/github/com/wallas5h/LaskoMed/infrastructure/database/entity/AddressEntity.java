package https.github.com.wallas5h.LaskoMed.infrastructure.database.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

  @NotBlank
  @Size(max = 32)
  @Column(name = "country")
  private String country;

  @NotBlank
  @Size(max = 32)
  @Column(name = "voivodeship")
  private String voivodeship;

  @NotBlank
  @Size(max = 32)
  @Column(name = "postal_code")
  private String postalCode;

  @NotBlank
  @Size(max = 10)
  @Column(name = "house_number")
  private String houseNumber;

  @NotBlank
  @Size(max = 32)
  @Column(name = "city")
  private String city;

  @Size(max = 32)
  @Column(name = "street")
  private String street;

  @Size(max = 10)
  @Column(name = "apartment_number")
  private String apartmentNumber;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
  private PatientEntity patient;

  @OneToOne(fetch = FetchType.LAZY, mappedBy = "address")
  private ClinicEntity clinic;
}
