package rmhub.mod.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;

@Slf4j
@Configuration
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class NotificationModuleConfig {

  @Bean
  public KafkaProducerBase<String, String> kafkaProducerBase() {
    return new KafkaProducerBase<>();
  }
}
