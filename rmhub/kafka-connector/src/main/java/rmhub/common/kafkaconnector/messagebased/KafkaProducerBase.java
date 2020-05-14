package rmhub.common.kafkaconnector.messagebased;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * A producer template of RmHub.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 * @author Than Do
 */
@Slf4j
public class KafkaProducerBase<K, V> implements KafkaProducible<K, V> {

  @Autowired
  KafkaTemplate<K, V> kafkaTemplate;

  @Override
  public ListenableFuture<SendResult<K, V>> send(String topic, V data) {
    log.info("Sending message to topic {}.", topic);
    return kafkaTemplate.send(topic, data);
  }

//	@Override
//	public ListenableFuture<SendResult<K, V>> send(String topic, K key, V data) {
//		log.info("Sending message {} with key {} to topic {}.", data, key, topic);
//		final var sendResult = kafkaTemplate.send(topic, key, data);
//		log.info("Sent message {} with key {} to topic {}.", data, key, topic);
//		return sendResult;
//	}
}
