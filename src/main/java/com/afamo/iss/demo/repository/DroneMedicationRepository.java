package com.afamo.iss.demo.repository;

import com.afamo.iss.demo.entity.DroneMedications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneMedicationRepository extends JpaRepository<DroneMedications,Long> {

    List<DroneMedications> findByDroneId(Long droneId);
    List<DroneMedications> findByMedicationId(Long medicationId);
}
