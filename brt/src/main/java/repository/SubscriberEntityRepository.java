package repository;

import entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberEntityRepository extends JpaRepository<SubscriberEntity, Long> {
}