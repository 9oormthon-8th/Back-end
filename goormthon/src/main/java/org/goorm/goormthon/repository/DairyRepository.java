package org.goorm.goormthon.repository;

import jakarta.persistence.EntityNotFoundException;
import org.goorm.goormthon.domain.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DairyRepository extends JpaRepository<Dairy, Long> {

    default Dairy findByIdOrThrow(Long dairyId) {
        return findById(dairyId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 기록이 없습니다."));
    }
}
