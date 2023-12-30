package com.afamo.iss.demo;
import com.afamo.iss.demo.config.AuditProperties;
import com.afamo.iss.demo.config.Model;
import com.afamo.iss.demo.config.State;
import com.afamo.iss.demo.entity.Drone;
import com.afamo.iss.demo.repository.MedicationRepository;
import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.MedicationRequestDTO;
import com.afamo.iss.demo.services.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.afamo.iss.demo.repository.DroneRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Slf4j
//@EnableJpaRepositories("")
@EnableScheduling
@EnableConfigurationProperties(AuditProperties.class)
@PropertySource(value = {"classpath:application.properties"})
//@EnableSwagger2
public class DroneRestApplication {
@Autowired MedicationRepository medicationRepository;
	public static void main(String[] args) {
		log.info("Starting DroneRestApplication....");
		SpringApplication.run(DroneRestApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(DroneService droneService){
		return (args)->{
		// populate the DB with some Drone data
			droneService.createAndReturnDroneInfo(new DroneRequestDTO(null,"111", Model.LIGHT_WEIGHT,100l,0.1, State.IDLE));
			droneService.createAndReturnDroneInfo(new DroneRequestDTO(null,"121", Model.HEAVY_WEIGHT,500l,0.2, State.IDLE));
			droneService.createAndReturnDroneInfo(new DroneRequestDTO(null,"131", Model.MIDDLE_WEIGHT,300l,0.8, State.IDLE));
			log.info("Last created Drone->{}",droneService.createAndReturnDroneInfo(new DroneRequestDTO(null,"141", Model.CRUISER_WEIGHT,500l,0.6, State.IDLE)));
			//populate the DB with some Medication Items

			droneService.createAndReturnMedicationInfo(new MedicationRequestDTO(null, "Artifeld",300.3,"AR-12","art.jpg"));
			droneService.createAndReturnMedicationInfo(new MedicationRequestDTO(null, "Panadol",100.8,"PA-31","pan.jpg"));
			log.info("Last created Medication Item->{}",droneService.createAndReturnMedicationInfo(new MedicationRequestDTO(null, "Detol",150.5,"DET_20-D","det.jpg")));
			log.info("Successfully Retrieved Med Items -->{}",medicationRepository.findAll());
			log.info("Initial Data Seeding successfully Done");
		};
	}

}
