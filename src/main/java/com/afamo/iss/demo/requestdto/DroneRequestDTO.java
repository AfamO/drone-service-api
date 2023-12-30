package com.afamo.iss.demo.requestdto;

import com.afamo.iss.demo.config.Model;
import com.afamo.iss.demo.config.State;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DroneRequestDTO {
    private Long id;
    private String serialNumber;
    private Model model;
    private Long weightLimit;
    private Double batteryCapacity;
    private State state;
}
