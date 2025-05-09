package brtApp.repository;

import brtApp.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity, Long> {

    Optional<SubscriberEntity> findByMsisdn(String msisdn);
    Optional<SubscriberEntity> findByPassportData(String passport);
}