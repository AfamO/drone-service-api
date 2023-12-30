package com.afamo.iss.demo.repository;

import com.afamo.iss.demo.entity.DroneAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneAuditLogRepository extends JpaRepository<DroneAuditLog,Long> {


    List<DroneAuditLog> findByDroneId(Long id);

    Page<DroneAuditLog> findAll(Pageable pageable);

    List<DroneAuditLog> findAllByBatteryCapacity(double battery_capacity);

    Optional<DroneAuditLog> findById(Long id);
}
