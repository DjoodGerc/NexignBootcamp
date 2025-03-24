package com.example.demo.repository;

import com.example.demo.entity.CdrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CdrRepo extends JpaRepository<CdrEntity,Long> {
    List<CdrEntity> findByInitiating_IdOrReceiving_Id(long initId, long recId);
    List<CdrEntity> findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(long initId,Timestamp startDate1, Timestamp endDate1, long recId, Timestamp startDate, Timestamp endDate);
    List<CdrEntity> findByStartCallBetween(Timestamp startDate, Timestamp endDate);
}
