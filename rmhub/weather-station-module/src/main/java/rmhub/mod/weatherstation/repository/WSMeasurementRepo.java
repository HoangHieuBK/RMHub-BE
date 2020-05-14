package rmhub.mod.weatherstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;

public interface WSMeasurementRepo extends JpaRepository<WeatherMeasurement, Long> {

}
