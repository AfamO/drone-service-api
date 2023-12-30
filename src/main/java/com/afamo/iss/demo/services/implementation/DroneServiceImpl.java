package com.afamo.iss.demo.services.implementation;

import com.afamo.iss.demo.config.State;
import com.afamo.iss.demo.entity.Drone;
import com.afamo.iss.demo.entity.DroneMedications;
import com.afamo.iss.demo.entity.Medication;
import com.afamo.iss.demo.repository.DroneMedicationRepository;
import com.afamo.iss.demo.repository.DroneRepository;
import com.afamo.iss.demo.repository.MedicationRepository;
import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.LoadDroneRequestDTO;
import com.afamo.iss.demo.requestdto.MedicationRequestDTO;
import com.afamo.iss.demo.responsedto.LoadedDroneResponseDto;
import com.afamo.iss.demo.responsedto.ResponseDto;
import com.afamo.iss.demo.services.DroneService;
import com.afamo.iss.demo.utility.Validation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private ResponseDto responseDto;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final Validation validation;
    private final MedicationRepository medicationRepository;
    private final DroneMedicationRepository droneMedicationRepository;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, ModelMapper modelMapper, ObjectMapper objectMapper, Validation validation, MedicationRepository medicationRepository, DroneMedicationRepository droneMedicationRepository) {
        this.droneRepository = droneRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.validation = validation;
        this.medicationRepository = medicationRepository;
        this.droneMedicationRepository = droneMedicationRepository;
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
    }


    @Override
    public ResponseDto createAndReturnDroneInfo(DroneRequestDTO droneRequestDTO) {
        //Validate Drone Registeration

        ResponseDto validationResponse = validation.validateDrone(droneRequestDTO);
        if (validationResponse.getHttpStatus() == HttpStatus.OK.value()){

            Drone drone = droneRepository.findBySerialNumber(droneRequestDTO.getSerialNumber());
            responseDto = new ResponseDto();
            if (drone!=null){
                responseDto.setMessage("Drone with serialNumber '"+droneRequestDTO.getSerialNumber()+"' already exists");
                responseDto.setHttpStatus(HttpStatus.CONFLICT.value());
                return responseDto;
            }
            drone = new Drone();
            drone = modelMapper.map(droneRequestDTO,Drone.class);
            drone = droneRepository.save(drone);
            responseDto.setHttpStatus(HttpStatus.CREATED.value());
            responseDto.setData(drone);
            return  responseDto;

        }
        return validationResponse;

    }

    @Override
    public ResponseDto createAndReturnMedicationInfo(MedicationRequestDTO medicationRequestDTO) {
        ResponseDto validationResponse = validation.validateMedication(medicationRequestDTO);
        if (validation.validateMedication(medicationRequestDTO).getHttpStatus() == HttpStatus.OK.value()){

            Medication medication = medicationRepository.findByCode(medicationRequestDTO.getCode());
            responseDto = new ResponseDto();
            if (medication!=null){
                responseDto.setMessage("Medication Item with '"+medicationRequestDTO.getCode()+"' already exists");
                responseDto.setHttpStatus(HttpStatus.CONFLICT.value());
                return responseDto;
            }
            medication = new Medication();
            medication = modelMapper.map(medicationRequestDTO,Medication.class);
            medication = medicationRepository.save(medication);
            responseDto.setHttpStatus(HttpStatus.CREATED.value());
            responseDto.setData(medication);
            return  responseDto;

        }
        else {

            return validationResponse;
        }
    }

    @Override
    public ResponseDto checkBatteryLevelOfADrone(Long droneId) {
        Optional<Drone> drone = droneRepository.findById(droneId);
        if (drone.isEmpty())
        {
            responseDto = new ResponseDto();
            responseDto.setMessage("Drone with id '"+droneId+"' is not found!");
            responseDto.setHttpStatus(HttpStatus.NOT_FOUND.value());
        }
        else {
            responseDto = new ResponseDto();
            responseDto.setData
                    (drone.get()
                            .getBatteryCapacity());
        }
        return responseDto;

    }

    @Override
    public ResponseDto findLoadedMedicationsForAGivenDrone(Long droneId) {
        List<DroneMedications> droneMedications  = droneMedicationRepository.findByDroneId(droneId);
        if (droneMedications.isEmpty())
        {
            responseDto = new ResponseDto();
            responseDto.setMessage("The Drone with id '"+droneId+"' has not been loaded");
            responseDto.setHttpStatus(HttpStatus.NOT_FOUND.value());
        }
        else {
            responseDto = new ResponseDto();
            Drone drone = droneRepository.getReferenceById(droneId);
            responseDto.setData(getLoadedDrones(drone, droneMedications));;
        }
        return responseDto;

    }

    @Override
    public ResponseDto getAllDronesNotYetLoaded() {
        responseDto = new ResponseDto();
        responseDto.setData(droneRepository.findDroneByState(State.IDLE));
        return responseDto;
    }

    @Override
    public ResponseDto loadDroneWithMedicationItems(LoadDroneRequestDTO loadDroneRequestDTO) {
            Drone drone = droneRepository.getReferenceById(loadDroneRequestDTO.droneId());
            if (drone == null) {
                responseDto = new ResponseDto();
                responseDto.setMessage("Drone with id '"+loadDroneRequestDTO.droneId()+"' is not found and hence cannot be loaded");
                responseDto.setHttpStatus(HttpStatus.NOT_FOUND.value());
                return responseDto;
            }
            else if (drone.getBatteryCapacity() < 0.25){
                responseDto = new ResponseDto();
                responseDto.setMessage("OOps! This Drone cannot be loaded, the battery level is less than 0.25");
                responseDto.setHttpStatus(HttpStatus.CONFLICT.value());
                responseDto.setData(drone);
                return responseDto;
            }
            Double totalWeight = 0d;
            //Calculate the total medications Weights
            List<DroneMedications> droneMedicationsList = new ArrayList<>();
            for ( Long medicalId : loadDroneRequestDTO.medicationIds()){
                Medication medication  = medicationRepository.getReferenceById(medicalId);
                totalWeight+= medication.getWeight();
                //Is the total weight of the medications less than drone's weight limit ?
                if (totalWeight <= drone.getWeightLimit() ){
                    //Then load it with medicalItem
                    DroneMedications droneMedications  = new DroneMedications();
                    droneMedications.setDroneId(drone.getId());
                    droneMedications.setMedicationId(medicalId);
                    droneMedicationsList.add(droneMedications);
                }
                else {
                    break;
                }
            }
            droneMedicationsList = droneMedicationRepository.saveAll(droneMedicationsList);
            log.info("Successfully saved drone medications {}",droneMedicationsList);
            drone.setState(State.LOADED);
            drone = droneRepository.save(drone); //update drone table
            responseDto = new ResponseDto();
            responseDto.setData(getLoadedDrones(drone, droneMedicationsList));
            return responseDto;
    }


    private LoadedDroneResponseDto getLoadedDrones(Drone drone, List<DroneMedications> droneMedications){
        List<Medication> medicationList = new ArrayList<>();
        for (DroneMedications droneMedication : droneMedications){
            Medication medication = medicationRepository.getReferenceById(droneMedication.getMedicationId());
            medicationList.add(medication);
        }
        LoadedDroneResponseDto loadedDroneResponseDto = new LoadedDroneResponseDto();
        loadedDroneResponseDto.setDrone(drone);
        loadedDroneResponseDto.setMedicationItems(medicationList);
        return loadedDroneResponseDto;

    }

    @Override
    public ResponseDto removeDrone(Long id) {
        responseDto = new ResponseDto();
        Optional<Drone> drone = droneRepository.findById(id);
        if (drone.isEmpty())
        {
            responseDto.setMessage("Drone with id '"+id+"' is not found");
            responseDto.setHttpStatus(HttpStatus.NOT_FOUND.value());
        }
        else {
            droneRepository.delete(drone.get());
            responseDto.setMessage("Delete Operation Successfull");
        }
        return responseDto;
    }

    @Override
    public ResponseDto getAllDrones(PageRequest pageRequest) {
        responseDto = new ResponseDto();
        responseDto.setData(droneRepository.findAll(pageRequest));
        return responseDto;
    }
}
