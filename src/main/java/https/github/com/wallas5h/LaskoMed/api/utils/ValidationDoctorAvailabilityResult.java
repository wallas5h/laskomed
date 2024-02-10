package https.github.com.wallas5h.LaskoMed.api.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationDoctorAvailabilityResult {
  private boolean isCorrect;
  private String message;

}
