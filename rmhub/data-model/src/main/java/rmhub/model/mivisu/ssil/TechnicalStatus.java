package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TechnicalStatus {

  @JsonProperty("Nb_Et")
  private int nb_Et;

  @JsonProperty("ET")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Et> ets;

  @JsonIgnoreProperties
  @Builder.Default
  private final Date receivedDate = new Date();

  @JsonIgnoreProperties
  private int deploymentId;
}
