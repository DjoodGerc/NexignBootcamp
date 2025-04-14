package com.example.demo.repository;

import com.example.demo.entity.CallEntity;
import org.aspectj.weaver.ast.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CallRepo extends JpaRepository<CallEntity, Long> {
    List<CallEntity> findByInitiating_IdOrReceiving_Id(long initId, long recId);

    List<CallEntity> findByInitiating_IdAndStartCallBetweenOrReceiving_IdAndStartCallBetween(long initId, Timestamp startDate1, Timestamp endDate1, long recId, Timestamp startDate, Timestamp endDate);

    List<CallEntity> findByStartCallBetween(Timestamp startDate, Timestamp endDate);

    long count();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE call_data", nativeQuery = true)
    void truncateTable();

}
