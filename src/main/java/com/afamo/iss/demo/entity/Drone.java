package com.afamo.iss.demo.entity;

import com.afamo.iss.demo.config.Model;
import com.afamo.iss.demo.config.State;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "drone")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "model")
    private Model model;

    @Column(name = "weight_limit")
    private Long weightLimit;

    @Column(name = "battery_capacity")
    private Double batteryCapacity;

    @Column(name = "state")
    private State state;



}
