package https.github.com.wallas5h.LaskoMed.infrastructure.configuration;

import https.github.com.wallas5h.LaskoMed.LaskoMedApplication;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SpringDocConfiguration {

  @Bean
  public GroupedOpenApi groupedOpenApi(){
    return GroupedOpenApi.builder()
        .group("default")
        .pathsToMatch("/**")
        .packagesToScan(LaskoMedApplication.class.getPackageName())
        .build();
  }

  @Bean
  public OpenAPI springDocOpenApi(){
    return new OpenAPI()
        .components(new Components())
        .info(new Info()
            .title("LaskoMed Medical Center")
            .contact(contact())
            .version("1.0")
        );
  }



  private Contact contact(){
    return  new Contact()
        .name("Waldemar Laskowski")
        .url("https://www.linkedin.com/in/waldemar-laskowski-183951238")
        .email("wallas5h@gmail.com");
  }

}

