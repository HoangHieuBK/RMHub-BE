package rmhub.mod.devicemgmt.config;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;

@Slf4j
@Configuration
@Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
public class DeviceManagementModuleConfig implements WebMvcConfigurer {

  /**
   * Specify message source files here, as parameters for {@link ReloadableResourceBundleMessageSource#setBasenames(String...)} method
   */
  @Bean
  public MessageSource messageSource() {
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    log.info("MessageSource loaded!");
    return messageSource;
  }

  /**
   * This is for implementing i18n for validation exception handlers
   */
  @Bean
  @Primary
  public LocalValidatorFactoryBean getValidator() {
    var bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

  @Bean
  public KafkaProducerBase<String, String> kafkaProducerBase() {
    return new KafkaProducerBase<>();
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  /**
   * Allow resource path with case-insensitive.
   */
  @Override
  public void configurePathMatch(PathMatchConfigurer configurer) {
    AntPathMatcher matcher = new AntPathMatcher();
    matcher.setCaseSensitive(false);
    configurer.setPathMatcher(matcher);
  }
}
