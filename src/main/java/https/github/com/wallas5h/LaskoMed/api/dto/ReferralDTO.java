package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferralDTO {

  private Long referralId;
  private OffsetDateTime referralDate;
  private String referralPurpose;
  private String referralDiagnosis;
  private AddressDTO addressDTO;
  private DoctorDTO doctor;
  private ClinicDTO clinicDTO;
  private PatientDTO patientDTO;
}
