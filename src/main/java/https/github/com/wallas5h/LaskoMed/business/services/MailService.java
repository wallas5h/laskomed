package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.infrastructure.Model.MailStructure;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
public class MailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Value("${spring.mail.username}")
  private String fromMail;
  @Value("${server.host}")
  private String host;
  @Value("${server.port}")
  private String port;
  @Value("${server.servlet.context-path}")
  private String contextPath;


  public void sendMail(MailStructure mailStructure) {
    try {
      SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
      simpleMailMessage.setFrom(fromMail);
      simpleMailMessage.setSubject(mailStructure.getSubject());
      simpleMailMessage.setText(mailStructure.getMessage());
      simpleMailMessage.setTo(mailStructure.getRecipient());

      mailSender.send(simpleMailMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void sendConfirmationEmail(UserEntity user, String confirmationToken) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(user.getEmail());
      helper.setSubject("Potwierdzenie rejestracji");

      Context context = new Context();
      context.setVariable("user", user);
      String confirmUrl = "http://%s:%s%s%s%s%s"
          .formatted(host, port, contextPath,AuthController.AUTH, AuthController.REGISTER_CONFIRM,confirmationToken);
      context.setVariable("confirmUrl", confirmUrl);
      String html = templateEngine.process("registrationConfirmation", context);

      helper.setText(html, true);

      mailSender.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
