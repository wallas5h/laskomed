package https.github.com.wallas5h.LaskoMed.api.controller;

import https.github.com.wallas5h.LaskoMed.business.services.MailService;
import https.github.com.wallas5h.LaskoMed.infrastructure.Model.MailStructure;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mail", description = "Methods for mail management")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping(MailController.BASE)
@AllArgsConstructor
public class MailController {
  public static final String BASE = "/mail";
  public static final String SEND = "/send";

  private MailService mailService;

  @PostMapping(SEND)
  public String sendMail(
      @RequestBody MailStructure mailStructure
      ){
    mailService.sendMail( mailStructure);
    return "Mail send successfully ";
  }
}
