package com.sfi.cfrmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class CFRMapApplication extends SpringBootServletInitializer{


	public static void main(String[] args) {
		SpringApplication.run(CFRMapApplication.class);
	}
//	@Bean public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
//	return (container -> {
//	container.setSessionTimeout(1000); // session timeout value
//	});
//	}

}
