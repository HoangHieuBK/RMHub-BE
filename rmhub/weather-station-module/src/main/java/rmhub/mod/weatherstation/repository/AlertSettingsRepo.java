package rmhub.mod.weatherstation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;

public interface AlertSettingsRepo extends JpaRepository<WeatherAlertRule, Long> {

  Optional<WeatherAlertRule> findById(Long id);

  @Query("Select p from WeatherAlertRule p where p.id = :id and p.status = :status")
  WeatherAlertRule findById(Long id, Boolean status);

  @Query("Select p from WeatherAlertRule p where p.status = :status and p.deploymentId = :deploymentId")
  List<WeatherAlertRule> find(Boolean status, Integer deploymentId);

  @Query("Select p from WeatherAlertRule p where p.alertCode = :alertCode and p.status = :status and p.deploymentId = :deploymentId")
  WeatherAlertRule find(String alertCode, Boolean status, Integer deploymentId);

  @Query("Select p from WeatherAlertRule p where  ( :id is null or  p.id <> :id  )  and p.value = :value and p.status = :status and p.deploymentId = :deploymentId ")
  List<WeatherAlertRule> findExistsRuleByValue(Long id, Integer value, Boolean status, Integer deploymentId);

  @Query("Select p from WeatherAlertRule p where p.alertCode like %:alertCode% and p.status = true and p.deploymentId = :deploymentId")
  List<WeatherAlertRule> findByAlertCode(String alertCode, Integer deploymentId);

}
