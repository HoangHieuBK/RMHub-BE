package rmhub.common.kafkaconnector.messagebased;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Slf4j
public class KafkaConsumerAspect {

  @Before(value = "@annotation(org.springframework.kafka.annotation.KafkaListener)")
  public void before(JoinPoint joinPoint) {
    log.info("before execution of {}", joinPoint);
    log.info("argument value {}", joinPoint.getArgs()[0].toString());
  }

  @After(value = "@annotation(org.springframework.kafka.annotation.KafkaListener)")
  public void after(JoinPoint joinPoint) {
    log.info("after execution of {}", joinPoint);
  }
}
