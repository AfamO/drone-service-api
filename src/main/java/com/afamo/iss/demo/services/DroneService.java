package com.afamo.iss.demo.services;

import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.LoadDroneRequestDTO;
import com.afamo.iss.demo.requestdto.MedicationRequestDTO;
import com.afamo.iss.demo.responsedto.ResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface DroneService {


    public ResponseDto createAndReturnDroneInfo(DroneRequestDTO droneRequestDTO);

    public ResponseDto createAndReturnMedicationInfo(MedicationRequestDTO droneRequestDTO);

    public ResponseDto checkBatteryLevelOfADrone(Long droneId);

    public ResponseDto findLoadedMedicationsForAGivenDrone(Long droneId);

    public ResponseDto getAllDronesNotYetLoaded();

    public ResponseDto loadDroneWithMedicationItems(LoadDroneRequestDTO loadDroneRequestDTO);

    public ResponseDto removeDrone(Long id);

    public ResponseDto getAllDrones(PageRequest pageRequest);
}
