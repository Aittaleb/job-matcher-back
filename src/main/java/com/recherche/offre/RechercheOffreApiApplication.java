package com.recherche.offre;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Offre API",
				version = "1.0",
				description = "Documentation de l'API pour la recherche d'offres d'emploi"
		)
)
@SpringBootApplication
@EnableFeignClients
@EnableCaching
public class RechercheOffreApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RechercheOffreApiApplication.class, args);
	}

}
