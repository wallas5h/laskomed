package https.github.com.wallas5h.LaskoMed.infrastructure.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailStructure {

  private String recipient;
  private String subject;
  private String message;
}
