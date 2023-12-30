package com.afamo.iss.demo.utility;

import com.afamo.iss.demo.entity.Medication;
import com.afamo.iss.demo.requestdto.DroneRequestDTO;
import com.afamo.iss.demo.requestdto.MedicationRequestDTO;
import com.afamo.iss.demo.responsedto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class Validation {

    public ResponseDto validateDrone(DroneRequestDTO droneRequestDTO){
        // Validation and testing for empty and null values
        ResponseDto responseDto = new ResponseDto();
        if (droneRequestDTO.getSerialNumber().length() > 100){
            responseDto.setMessage("Serial number characters cannot be more than 100");
            responseDto.setHttpStatus(HttpStatus.CONFLICT.value());
            return responseDto;
        }
        else if (droneRequestDTO.getWeightLimit() > 500){
            responseDto.setMessage("The drone's weight cannot be more than 500gr");
            responseDto.setHttpStatus(HttpStatus.CONFLICT.value());
            return responseDto;
        }
        //Then test for other things such as correct  Model and State values
        else {
            return  responseDto;
        }
    }

    public ResponseDto validateMedication(MedicationRequestDTO medicationRequestDTO){
        ResponseDto responseDto = new ResponseDto();
        if (medicationRequestDTO.getName() == null){
            responseDto.setMessage("The medication must have a name which cannot be null");
            responseDto.setHttpStatus(HttpStatus.CONFLICT.value());

            return responseDto;
        }
        if (medicationRequestDTO.getCode() == null){
            responseDto.setMessage("The medication must have a code which cannot be null");
            responseDto.setHttpStatus(HttpStatus.CONFLICT.value());

            return responseDto;
        }
        //Validation and testing for empty and null values of other Medication fields

        //Then test for other things such as correct  Model and State values
        else {
            return  responseDto;
        }
    }
}
