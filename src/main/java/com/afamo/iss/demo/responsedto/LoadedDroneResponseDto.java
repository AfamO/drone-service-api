package com.afamo.iss.demo.responsedto;

import com.afamo.iss.demo.entity.Drone;
import com.afamo.iss.demo.entity.Medication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoadedDroneResponseDto {
    private Drone drone;
    private List<Medication> medicationItems;
}
