package com.afamo.iss.demo.repository;

import com.afamo.iss.demo.config.State;
import com.afamo.iss.demo.entity.Drone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone,Long> {

    Drone findBySerialNumber(String serialNumber);

    List<Drone> findDroneByState(State state);

    Page<Drone> findAll(Pageable pageable);

    List<Drone> findAllByBatteryCapacity(Double battery_capacity);

    Optional<Drone> findById(Long id);
}
