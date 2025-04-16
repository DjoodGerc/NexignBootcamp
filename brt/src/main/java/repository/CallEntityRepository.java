package repository;

import entity.CallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallEntityRepository extends JpaRepository<CallEntity, Long> {
}