package com.afamo.iss.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "drone_audit_log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneAuditLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "drone_id")
    private Long droneId;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @Column(name = "battery_capacity")
    private Double batteryCapacity;



}
