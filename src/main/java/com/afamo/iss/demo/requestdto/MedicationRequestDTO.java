package com.afamo.iss.demo.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicationRequestDTO {
    private Long id;
    private String name;
    private Double weight;
    private String code;
    private String image;

}
