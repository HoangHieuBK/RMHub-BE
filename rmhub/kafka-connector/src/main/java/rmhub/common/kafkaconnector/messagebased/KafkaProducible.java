package rmhub.common.kafkaconnector.messagebased;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * The basic Kafka Producible of RmHub returning {@link ListenableFuture}s.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 *
 * @author Than Do
 */
public interface KafkaProducible<K, V> {
	/**
	 * Send the data to the provided topic with no key or partition.
	 * @param topic the topic.
	 * @param data The data.
	 * @return a Future for the {@link SendResult}.
	 */
	ListenableFuture<SendResult<K, V>> send(String topic, V data);

	/**
	 * Send the data to the provided topic with the provided key and no partition.
	 * @param topic the topic.
	 * @param key the key.
	 * @param data The data.
	 * @return a Future for the {@link SendResult}.
	 */
//	ListenableFuture<SendResult<K, V>> send(String topic, K key, V data);
}
