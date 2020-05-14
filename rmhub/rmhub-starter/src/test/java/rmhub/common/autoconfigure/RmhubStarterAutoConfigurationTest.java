package rmhub.common.autoconfigure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rmhub.common.handler.RmhubExceptionHandler;

/**
 * Verify the auto-configuration.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Slf4j
public class RmhubStarterAutoConfigurationTest {

  private final String RMHUB_EXCEPTION_HANDLER_BEAN_NAME = "rmhubExceptionHandler";

  /**
   * Webapp Context with {@link RmhubStarterAutoConfiguration}
   */
  private final WebApplicationContextRunner contextRunner =
      new WebApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(RmhubStarterAutoConfiguration.class));

  /**
   * When a module configure its own exception handler with {@link RestControllerAdvice} annotation, we don't need auto-config for {@link
   * RmhubExceptionHandler}
   */
  @Test
  public void whenExceptionHandlerIsPresent_thenNotAutoConfig() {

    this.contextRunner.withUserConfiguration(TestRmhubExceptionHandlerConfiguration.class).run(context -> {

      log.info("RestControllerAdvice bean: {}", context.getBeanFactory().getBeansWithAnnotation(RestControllerAdvice.class));

      // verify that only one exception handler is injected
      assertThat(context.getBeanFactory().getBeansWithAnnotation(RestControllerAdvice.class).size()).isEqualTo(1);

      // verify that the advice bean is an TestExceptionHandler class instance
      assertThat(context.getBeanFactory().getBeansWithAnnotation(RestControllerAdvice.class).values().toArray()[0])
          .isExactlyInstanceOf(TestExceptionHandler.class);

      // verify that no auto-config happened
      assertThat(context).doesNotHaveBean(RMHUB_EXCEPTION_HANDLER_BEAN_NAME);
    });
  }

  /**
   * When a module doesn't configure any exception handler, we auto-config with {@link RmhubExceptionHandler}
   */
  @Test
  public void whenExceptionHandlerIsNotPresent_thenAutoConfig() {

    this.contextRunner.run(context -> {

      log.info("Names of Beans in factory: {}", Arrays.asList(context.getBeanDefinitionNames()));

      log.info("RestControllerAdvice bean: {}", context.getBeanFactory().getBeansWithAnnotation(RestControllerAdvice.class));

      // verify that the auto-config happened
      assertThat(context).hasBean(RMHUB_EXCEPTION_HANDLER_BEAN_NAME);

      assertThat(context).getBean(RMHUB_EXCEPTION_HANDLER_BEAN_NAME).isExactlyInstanceOf(RmhubExceptionHandler.class);
    });
  }

  /**
   * This config simulate when we create a module and implement an exception handler.
   */
  @Configuration
  @ComponentScan(basePackageClasses = TestExceptionHandler.class)
  static class TestRmhubExceptionHandlerConfiguration {

  }

  @RestControllerAdvice
  static class TestExceptionHandler extends RmhubExceptionHandler {

  }
}
