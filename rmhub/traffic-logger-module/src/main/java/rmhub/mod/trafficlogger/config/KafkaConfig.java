package rmhub.mod.trafficlogger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;

@Profile("!test")
@Configuration
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class KafkaConfig {

  @Bean
  public KafkaProducerBase<String, String> kafkaProducerBase() {
    return new KafkaProducerBase<>();
  }
}
