package rmhub.mod.trafficlogger.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import rmhub.mod.trafficlogger.dto.MinMaxDto;

@Disabled("need to set up the validator with spring configuration")
@Slf4j
class MinMaxCorrelationValidatorTest {

  private static ValidatorFactory VALIDATOR_FACTORY;

  private static Validator VALIDATOR;

  @BeforeAll
  private static void setUp() {
    VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    log.info("validators: {}", VALIDATOR_FACTORY.getValidator());
    VALIDATOR = VALIDATOR_FACTORY.getValidator();
  }

  @Test
  void bothMinAndMaxAreNull() {

    // given
    var sample = MinMaxDto.builder().min(null).max(null).build();

    // when
    Set<ConstraintViolation<MinMaxDto>> violations = VALIDATOR.validate(sample);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void onlyMinIsNull() {

    // given
    var sample = MinMaxDto.builder().min(null).max(100).build();

    // when
    Set<ConstraintViolation<MinMaxDto>> violations = VALIDATOR.validate(sample);

    // then
    assertTrue(violations.isEmpty());
  }

  @Test
  void onlyMaxIsNull() {

    // given
    var sample = MinMaxDto.builder().min(1).max(null).build();

    // when
    Set<ConstraintViolation<MinMaxDto>> violations = VALIDATOR.validate(sample);

    // then
    assertTrue(violations.isEmpty());
  }

  @Disabled("need to double-check")
  @Test
  void minIsGreaterThanMax() {

    // given
    var sample = MinMaxDto.builder().min(10).max(1).build();

    // when
    Set<ConstraintViolation<MinMaxDto>> violations = VALIDATOR.validate(sample);

    // then
    assertEquals(1, violations.size());
  }

  @Test
  void minIsLessThanMax() {

    // given
    var sample = MinMaxDto.builder().min(1).max(10).build();

    // when
    Set<ConstraintViolation<MinMaxDto>> violations = VALIDATOR.validate(sample);

    // then
    assertTrue(violations.isEmpty());
  }

  @AfterAll
  private static void cleanUp() {
    VALIDATOR_FACTORY.close();
  }
}
