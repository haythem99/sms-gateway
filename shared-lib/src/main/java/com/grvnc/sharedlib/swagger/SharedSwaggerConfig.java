package com.grvnc.sharedlib.swagger;

 import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
    
   
      public class SharedSwaggerConfig {
        private static final String SCHEME_NAME = "bearerScheme";
        private static final String SCHEME = "Bearer";
        
        @Bean
        public OpenAPI customOpenAPI() {
            var openApi = new OpenAPI()
                    .info(getInfo());
    
            addSecurity(openApi);
    
            return openApi;
        }

        private Info getInfo() {
            return new Info()
                    .title("Otluo Documentation Center")
                    .description("you can try any api directly from here")
                    .version("1.0.0")
                    .license(getLicense());
        }
    
        private License getLicense() {
            return new License()
                    .name("All Rights Reserved")
                    .url("https://www.linkedin.com/in/haythem-alyahya-8b031793/");
        }
        private void addSecurity(OpenAPI openApi) {
            var components = createComponents();
            var securityItem = new SecurityRequirement().addList(SCHEME_NAME);
    
            openApi
                    .components(components)
                    .addSecurityItem(securityItem);
        }
    
        private Components createComponents() {
            var components = new Components();
            components.addSecuritySchemes(SCHEME_NAME, createSecurityScheme());
    
            return components;
        }
    
        private SecurityScheme createSecurityScheme() {
            return new SecurityScheme()
                    .name(SCHEME_NAME)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme(SCHEME);
        }
        
        @Bean
        public OperationCustomizer customGlobalHeaders() {

            return (Operation operation, HandlerMethod handlerMethod) -> {

                Parameter missingParam1 = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema())
                        .name("client-name")
                        .description("available clients: web-desktop,web-mobile,ios,android")
                        .required(true);
                        
                Parameter missingParam2 = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema())
                        .name("client-passcode")
                        .required(true);

//                operation.addParametersItem(missingParam1);
//                operation.addParametersItem(missingParam2);

                return operation;
            };
        }
    
    }
