package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonRootName(value = "CORPS")
public class AlertResponse {

  @JsonProperty("deployment_id")
  private int deploymentId;

  @JsonProperty("Rep_Nb_Eqt")
  private int size;

  @JsonProperty("CENTRALE")
  private List<CentraleResponse> centraleResponses;

  @JsonIgnoreProperties
  @Builder.Default
  private final Date receivedDate = new Date();
}
