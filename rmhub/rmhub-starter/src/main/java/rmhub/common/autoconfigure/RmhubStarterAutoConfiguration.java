package rmhub.common.autoconfigure;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rmhub.common.handler.RmhubExceptionHandler;

/**
 * Auto-configuration for modules that make use of rmhub-starter.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class RmhubStarterAutoConfiguration {

  /**
   * Register {@link RmhubExceptionHandler} for exception handling if no {@link RestControllerAdvice} bean is specified.
   */
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  @ConditionalOnMissingBean(annotation = RestControllerAdvice.class, search = SearchStrategy.CURRENT)
  RmhubExceptionHandler rmhubExceptionHandler() {
    return new RmhubExceptionHandler();
  }
}
