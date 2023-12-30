package com.afamo.iss.demo;

import com.afamo.iss.demo.config.Model;
import com.afamo.iss.demo.config.State;
import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.LoadDroneRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class SpringDataRestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private DroneRequestDTO droneRequestDTO;

	private LoadDroneRequestDTO loadDroneRequestDTO;


	@Before("")
	public void setup() {
	}

	@Test
	void contextLoads() {
	}

	//Test api/endpoint 1: Drone registeration
	@Test
	public void testDroneRegisteration() throws Exception {
		droneRequestDTO = new DroneRequestDTO(null,"419", Model.LIGHT_WEIGHT,100l,0.1, State.IDLE);
		mockMvc.perform(post("/drones/api/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(droneRequestDTO))
				)
				.andExpect(jsonPath("$.httpStatus").value(201))
				.andExpect(jsonPath("$.message").value("Successfull"))
				.andExpect(jsonPath("$.data.model").value("LIGHT_WEIGHT"))
				.andDo(print())
				.andExpect(
						status().isOk());
	}

	//Test api/endpoint 2: Load Drones With Medication Items
	@Test
	public void testLoadDroneWithMedicationItems() throws Exception {
		loadDroneRequestDTO = new LoadDroneRequestDTO(3L, List.of(2L,3L));
		mockMvc.perform(put("/drones/api/load/MedicationItems")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(loadDroneRequestDTO))
				)
				.andExpect(jsonPath("$.httpStatus").value(200))
				.andExpect(jsonPath("$.message").value("Successfull"))
				.andExpect(jsonPath("$.data.drone.state").value("LOADED"))
				.andExpect(jsonPath("$.data.medicationItems[0].name").value("Panadol"))
				.andDo(print())
				.andExpect(
						status().isOk());
	}
	//Test api/endpoint 3: Check loaded medication items for a given drone
	@Test
	public void checkMedicationsItemsForGivenDrone() throws Exception {
		mockMvc.perform(get("/drones/api/check/MedicationItems/2"))
				.andDo(print())
				.andExpect(jsonPath("$.message").value("The Drone with id '2' has not been loaded"))
				.andExpect(
						status().isOk());
	}

	//Test api/endpoint 4: Check available drones for loading
	@Test
	public void checkAvailableDronesForLoading() throws Exception {
		mockMvc.perform(get("/drones/api/check/AvailableDronesForLoading"))
				.andDo(print())
				.andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(
						status().isOk());
	}

	//Test api/endpoint 5: Check drone battery level for a given drone
	@Test
	public void checkBatteryLevelForGivenDrone() throws Exception {
		mockMvc.perform(get("/drones/api/check/batteryLevel/1"))
				.andDo(print())
				.andExpect(jsonPath("$.data").value(0.1))
				.andExpect(
						status().isOk());
	}
}
