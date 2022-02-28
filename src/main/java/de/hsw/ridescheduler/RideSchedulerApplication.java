package de.hsw.ridescheduler;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class RideSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideSchedulerApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/busLines/**").allowedOrigins("http://localhost:3000");
				registry.addMapping("/busStops/**").allowedOrigins("http://localhost:3000");
				registry.addMapping("/schedules/**").allowedOrigins("http://localhost:3000");
			}
		};
	}
}
