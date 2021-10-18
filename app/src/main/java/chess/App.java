package chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Chess API", version = "2.0", description = "Backend for Chess Web App"))
@SecurityScheme(name="chess-api", scheme="bearer", type=SecuritySchemeType.HTTP, in=SecuritySchemeIn.HEADER)
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
    
  public String greeting() {
    return "Hello World";
  }

}

