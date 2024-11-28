package com.noom.interview.fullstack.sleep.db.repository;

import com.noom.interview.fullstack.sleep.db.entity.SleepEntity;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SleepRepository extends JpaRepository<SleepEntity, Long> {

  Optional<SleepEntity> findFirstByUserIdOrderBySleepDayDesc(long userId);

  List<SleepEntity> findAllByUserIdAndSleepDayGreaterThan(long userId, Date startDate);
}
