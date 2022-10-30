package com.ucu.taisback;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@SpringBootApplication
public class TaisBackApplication {

	public static void main(String[] args) throws IOException {

		ClassLoader classLoader = TaisBackApplication.class.getClassLoader();


		FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

		FirebaseOptions options = new FirebaseOptions.Builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.build();

		FirebaseApp.initializeApp(options);

		SpringApplication.run(TaisBackApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter(){
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("https://qrlink-dev.web.app/"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin","Content-Type",
				"Accept","Authorization","Origin", "Accept", "X-Requested-With","Access-Control-Request-Method",
				"Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Authorization",
				"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}