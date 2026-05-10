package com.alessandromelo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public OpenAPI apiInfo(){
        return new OpenAPI()
                .info(new Info()
                        .title("Product Manager API")
                        .version("1.0.0")
                        .description("API for managing a cosmetics business — control your products, stock, customers and sales in one place.")
                        .contact(new Contact()
                                .name("Alessandro Melo")
                                .url("https://www.linkedin.com/in/alessandro-melo-dev/")));
    }


}
