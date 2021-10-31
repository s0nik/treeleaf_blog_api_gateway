package com.blog.user.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    List<SecurityRequirement> arrayList = new ArrayList<>();
    arrayList.add(new SecurityRequirement().addList("bearerAuth"));
    SpringDocUtils.getConfig().replaceWithClass(org.springframework.data.domain.Pageable.class,
        org.springdoc.core.converters.models.Pageable.class);
    OpenAPI openAPI = new OpenAPI()
        .info(new Info().title("Blog User API").description("Treeleaf qualification task ").version("v0.0.1")
            .license(new License().name("Apache 2.0").url("http://springdoc.org")))
        .components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme().name("bearerAuth")
            .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
        .security(arrayList);
    return openAPI;
  }

}
