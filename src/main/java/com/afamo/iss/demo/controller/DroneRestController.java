package com.afamo.iss.demo.controller;

import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.LoadDroneRequestDTO;
import com.afamo.iss.demo.responsedto.ResponseDto;
import com.afamo.iss.demo.services.DroneService;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("drones/api/")
public class DroneRestController {

    private final DroneService droneService;

    public DroneRestController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("register")
    public ResponseDto registerDrone(@RequestBody DroneRequestDTO droneRequestDTO){
        return droneService.createAndReturnDroneInfo(droneRequestDTO);
    }
    @GetMapping("check/batteryLevel/{droneId}")
    public ResponseDto getSingleDrone(@PathVariable(name = "droneId",required = true) Long droneId){
        //ResponseEntity.status(204).build();
        return droneService.checkBatteryLevelOfADrone(droneId);
    }
    @GetMapping("get/all")
    public ResponseDto getAllPaginatedDrones(@RequestParam(name = "page")int page, @RequestParam(name = "size",defaultValue = "3")int size){
        return droneService.getAllDrones(PageRequest.of(page,size));
    }
    @GetMapping("check/MedicationItems/{droneId}")
    public ResponseDto checkMedicationsItemsForADrone(@PathVariable(name = "droneId",required = true) Long droneId){
        return droneService.findLoadedMedicationsForAGivenDrone(droneId);
    }
    @PutMapping("load/MedicationItems")
    private ResponseDto loadDroneWithMedicationItems(@Validated @RequestBody LoadDroneRequestDTO loadDroneRequestDTO){
        return droneService.loadDroneWithMedicationItems(loadDroneRequestDTO);
    }

    @GetMapping("check/AvailableDronesForLoading")
    public ResponseDto checkMedicationsItemsForADrone(){
        return droneService.getAllDronesNotYetLoaded();
    }
    @DeleteMapping("remove/single")
    private ResponseDto removeSingleCustomer(@RequestParam(value = "id")long id){
        return droneService.removeDrone(id);
    }
}

