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
public class DiagnosedDiseaseDTO {

  private Long diagnosedDiseaseId;
  private OffsetDateTime diagnosisDate;
  private String diseaseName;
  private String description;
}
