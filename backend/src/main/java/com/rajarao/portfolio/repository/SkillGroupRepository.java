package com.rajarao.portfolio.repository;

import com.rajarao.portfolio.model.SkillGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillGroupRepository extends JpaRepository<SkillGroup, Long> {
    List<SkillGroup> findAllByOrderBySortOrderAsc();
}
