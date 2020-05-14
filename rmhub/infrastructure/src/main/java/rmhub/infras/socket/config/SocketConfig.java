package rmhub.infras.socket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import rmhub.infras.socket.SocketIO;

@Configuration
@Slf4j
public class SocketConfig {

  @Value("${spring.task.execution.pool.core-size}")
  private int corePoolSize;

  @Value("${spring.task.execution.pool.max-size}")
  private int maxPoolSize;

  @Value("${spring.task.execution.thread-name-prefix}")
  private String namePrefix;

  @Value("${spring.task.execution.pool.queue-capacity}")
  private int queueCapacity;

  @Value("${spring.task.execution.pool.keep-alive}")
  private int keepAlive;

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setThreadNamePrefix(namePrefix);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAlive);
    executor.initialize();
    log.info("Bean Task execute created.");
    return executor;
  }

  @Bean
  @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

  public SocketIO getSocketIO() {
    SocketIO io = new SocketIO();
    log.info("Bean socket io created.");
    return io;
  }
}
