package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheWeatherMeasure {

  @JsonProperty("CORPS")
  AlertResponse alertResponse;
}
