package brtApp.repository;

import brtApp.entity.CallTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallTypeRepository extends JpaRepository<CallTypeEntity,Long> {
}
