package com.afamo.iss.demo.requestdto;

import java.util.List;

public record LoadDroneRequestDTO(Long droneId, List<Long> medicationIds) {
}
