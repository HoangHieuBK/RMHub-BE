package rmhub.mod.weatherstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.weatherstation.entity.WeatherMeasurementDetail;

public interface WSMeasurementDetailRepo extends JpaRepository<WeatherMeasurementDetail, Long> {

}
