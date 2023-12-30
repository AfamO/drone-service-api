package com.afamo.iss.demo.repository;

import com.afamo.iss.demo.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication,Long> {
    Medication findByCode(String code);

}
