package rmhub.mod.weatherstation.behaviour;

import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.MivisuXml;

public interface GenerateWeatherAlert {

  void process(MivisuXml data);

  void generateAlert(Body body, int deploymentId);
}
