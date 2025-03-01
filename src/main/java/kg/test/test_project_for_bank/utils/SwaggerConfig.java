package kg.test.test_project_for_bank.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Test Bank API")
                        .version("1.0")
                        .description("REST API для управления пользователями и их счетами в банковской системе. <br>" +
                                "Позволяет создавать пользователей и счета, пополнять баланс, списывать средства. <br>" +
                                "Если у вас остались вопросы, то свяжитесь по почте снизу.")
                        .contact(new Contact()
                                .email("malikkadyraliev447@gmail.com")
                                .name("Малик Кадыралиев"))
                );
    }
}
