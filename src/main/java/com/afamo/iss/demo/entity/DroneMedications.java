package com.afamo.iss.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "drone_medications")
@AllArgsConstructor
@NoArgsConstructor
@Data

public class DroneMedications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "drone_id")
    private Long droneId;

    @Column(name = "medication_id")
    private Long medicationId;

}
