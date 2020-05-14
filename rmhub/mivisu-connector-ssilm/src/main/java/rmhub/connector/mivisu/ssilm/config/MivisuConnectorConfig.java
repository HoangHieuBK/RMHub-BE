package rmhub.connector.mivisu.ssilm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;

@Configuration
@EnableScheduling
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class MivisuConnectorConfig {

  @Bean
  public KafkaProducerBase<String, String> kafkaProducerBase() {
    return new KafkaProducerBase<>();
  }
}
