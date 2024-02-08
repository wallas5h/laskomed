package https.github.com.wallas5h.LaskoMed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class ExceptionMessage {
  String errorId;
}
