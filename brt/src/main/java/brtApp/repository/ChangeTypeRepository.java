package brtApp.repository;

import brtApp.entity.ChangeTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeTypeRepository extends JpaRepository<ChangeTypeEntity, Long> {
}