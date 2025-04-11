package com.example.demo.repository;

import com.example.demo.entity.CallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CallRepo extends JpaRepository<CallEntity, Long> {
    List<CallEntity> findByInitiating_IdOrReceiving_Id(long initId, long recId);

    List<CallEntity> findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(long initId, Timestamp startDate1, Timestamp endDate1, long recId, Timestamp startDate, Timestamp endDate);

    List<CallEntity> findByStartCallBetween(Timestamp startDate, Timestamp endDate);

}
