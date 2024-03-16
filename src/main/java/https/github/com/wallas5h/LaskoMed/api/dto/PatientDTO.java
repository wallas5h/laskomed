package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

  private Long patientId;
  private String name;
  private String surname;
  private String pesel;
  private LocalDate birthdate;
  private String email;
  private String phone;
  private String medicalPackage;
  private String gender;
  private AddressDTO address;
  private UserDtoId appUser;
  private Set<MedicalAppointmentDTO> appointments;
  private Set<BookingAppointmentDTO> bookings;
  private Set<PrescriptionDTO> prescriptions;
  private Set<ReferralDTO> referrals;
  private Set<DiagnosedDiseaseDTO> diagnosedDiseases;

}
