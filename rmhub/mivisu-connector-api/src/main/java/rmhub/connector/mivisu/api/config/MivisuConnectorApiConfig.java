package rmhub.connector.mivisu.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;

@Configuration
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class MivisuConnectorApiConfig {

  @Bean
  public KafkaProducerBase<String, String> kafkaProducerBase() {
    return new KafkaProducerBase<>();
  }
}
