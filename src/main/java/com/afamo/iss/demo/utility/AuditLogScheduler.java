package com.afamo.iss.demo.utility;

import com.afamo.iss.demo.config.AuditProperties;
import com.afamo.iss.demo.entity.Drone;
import com.afamo.iss.demo.entity.DroneAuditLog;
import com.afamo.iss.demo.repository.DroneAuditLogRepository;
import com.afamo.iss.demo.repository.DroneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AuditLogScheduler {

  private final DroneRepository droneRepository;
  private final DroneAuditLogRepository droneAuditLogRepository;

    private final AuditProperties auditProperties;
    public AuditLogScheduler(DroneRepository droneRepository, DroneAuditLogRepository droneAuditLogRepository, AuditProperties auditProperties) {
        this.droneRepository = droneRepository;
        this.droneAuditLogRepository = droneAuditLogRepository;
        this.auditProperties = auditProperties;
        log.info("Audit Creation Rate===>{}",auditProperties.getCreationRate());
        log.info("Audit Retrieval Rate===>{}",auditProperties.getRetrievalRate());
    }

    //Every 15 seconds
    @Scheduled(fixedRate = 15000)
    public void scheduleAuditEventLog(){
        //Assume page size is 5, page is 0
        int page = 0; int size =5;
        //get each Drone
        List<Drone> droneList = droneRepository.findAll(PageRequest.of(page,size)).getContent();
        // then loop and create an audit log of their respective battery level
        List<DroneAuditLog> droneAuditLogList = new ArrayList<>();
        for (Drone drone : droneList){
            DroneAuditLog droneAuditLog = new DroneAuditLog(null, drone.getId(), LocalDateTime.now(),drone.getBatteryCapacity());
            droneAuditLogList.add(droneAuditLog);
        }
        //Now save all to DB
        droneAuditLogList = droneAuditLogRepository.saveAll(droneAuditLogList);
        log.info("Successfully Created AuditEventLogs ==>{}",droneAuditLogList);
    }

    //Every 20 seconds
    @Scheduled(fixedRate = 20000)
    public void scheduleAuditEventLogRetrieval(){
        //Assume page size is 5, page is 0
        int page = 0; int size =15;
        //Now retrieve current Audited logs;
        log.info("Successfully Retrieved AuditEventLogs ==>{}",droneAuditLogRepository.findAll(PageRequest.of(page,size)).getContent());

    }

}
