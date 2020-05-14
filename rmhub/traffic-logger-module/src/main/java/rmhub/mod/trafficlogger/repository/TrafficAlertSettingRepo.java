package rmhub.mod.trafficlogger.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rmhub.mod.trafficlogger.common.StatusEnum;
import rmhub.mod.trafficlogger.dto.business.SpeedSettingDto;
import rmhub.mod.trafficlogger.entity.TrafficAlertSetting;

public interface TrafficAlertSettingRepo extends JpaRepository<TrafficAlertSetting, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE TrafficAlertSetting s SET s.status = :status WHERE s.id = :id")
  void setStatus(@Param("id") Long id, @Param("status") StatusEnum status);

  boolean existsByIdAndStatusNot(Long id, StatusEnum status);

  // TODO will update later to avoid conflict
  List<TrafficAlertSetting> findAllByStatusNot(StatusEnum status);

  // TODO will update later to avoid conflict
  List<TrafficAlertSetting> findAllByStatusNotOrderByCreatedDateAsc(StatusEnum status);

  int countByStatus(StatusEnum status);

  @Query("select new rmhub.mod.trafficlogger.dto.business.SpeedSettingDto(s.min as min, s.max as max) from "
      + "TrafficAlertSetting s where s.status = :status order by s.min asc")
  List<SpeedSettingDto> findSpeedSettingByStatus(@Param("status") StatusEnum status);

  @Query("select new rmhub.mod.trafficlogger.dto.business.SpeedSettingDto(s.min as min, s.max as max) from "
      + "TrafficAlertSetting s where s.status = :status and s.id <> :id order by s.min asc")
  List<SpeedSettingDto> findSpeedSettingByStatusAndIdNot(@Param("status") StatusEnum status, @Param("id") Long id);
}
