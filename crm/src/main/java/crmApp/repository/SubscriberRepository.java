package crmApp.repository;

import crmApp.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<SubscriberEntity,Long> {
    Optional<SubscriberEntity> findByLogin(String login);
    Optional<SubscriberEntity> findByPassportData(String password);

}
