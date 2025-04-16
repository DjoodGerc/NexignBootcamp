package repository;

import entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffEntityRepository extends JpaRepository<TariffEntity, Long> {
}