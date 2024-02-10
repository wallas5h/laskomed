package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.*;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DoctorsDTO {

  private List<DoctorDTO> doctors;
}
