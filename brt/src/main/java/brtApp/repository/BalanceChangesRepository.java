package brtApp.repository;

import brtApp.entity.BalanceChangesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceChangesRepository extends JpaRepository<BalanceChangesEntity, Long> {
}