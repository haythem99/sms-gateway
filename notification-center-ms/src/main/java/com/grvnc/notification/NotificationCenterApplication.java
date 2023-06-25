package com.grvnc.notification;

import java.util.List;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.grvnc.sharedlib.repo.CustomRepositoryImpl;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories(repositoryBaseClass = CustomRepositoryImpl.class)
@EnableCaching
@EnableFeignClients
@EnableTransactionManagement
@EnableScheduling
public class NotificationCenterApplication {
	@Autowired
	private Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(NotificationCenterApplication.class, args);
	}

	
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setProperty("relaxedQueryChars", "|{}[]");
	        }
	    });
	    return factory;
	}
//	@Bean
//	public OpenAPI customOpenAPIForSwagger() {
//		Server server = new Server();
//		String contextPath = env.getProperty("server.servlet.context-path");
//		String port = env.getProperty("server.port");
//		String host = "otluo.com";
//		if (System.getProperty("os.name").toLowerCase().contains("win")) {
//			host = "localhost";
//		}
//		server.setUrl("http://" + host + ":" + port + contextPath);
//		return new OpenAPI().servers(List.of(server));
//	}
}
