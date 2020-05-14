package rmhub.mod.weatherstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.weatherstation.entity.WeatherAlert;

public interface WeatherAlertDataRepo extends JpaRepository<WeatherAlert, Long> {

}
