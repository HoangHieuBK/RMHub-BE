package rmhub.common.kafkaconnector.configs;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import rmhub.common.kafkaconnector.constant.KafkaConstant;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.group-id}")
  private String group;

  public Map<String, Object> consumerConfigs() {
    var props = new HashMap<String, Object>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstant.GROUP_ID_CONFIG);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConstant.MAX_POLL_RECORDS);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConstant.OFFSET_RESET_EARLIER);

    return props;
  }

  @Bean
  public <T> ConsumerFactory<String, T> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public <T> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, T>> kafkaListenerContainerFactory() {
    var factory = new ConcurrentKafkaListenerContainerFactory<String, T>();
    factory.setConsumerFactory(consumerFactory());

    return factory;
  }

  // @Bean
  // public ConsumerAwareListenerErrorHandler listenErrorHandler() {
  // return (m, e, c) -> {
  // MessageHeaders headers = m.getHeaders();
  // c.seek(new org.apache.kafka.common.TopicPartition(headers.get(KafkaHeaders.RECEIVED_TOPIC,
  // String.class),
  // headers.get(KafkaHeaders.RECEIVED_PARTITION_ID, Integer.class)),
  // headers.get(KafkaHeaders.OFFSET, Long.class));
  // log.error("Error when listening message {}", m);
  // return null;
  // };
  // }
}
