package rmhub.mod.trafficlogger.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import rmhub.mod.trafficlogger.dto.MinMaxDto;

/**
 * This validator is tightly coupled to validate min and max for {@link MinMaxDto} and its descendants.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
public class MinMaxCorrelationValidator implements ConstraintValidator<MinMaxCorrelation, MinMaxDto> {

  /**
   * Implements the validation logic. The state of {@code value} must not be altered.
   * <p>
   * This method can be accessed concurrently, thread-safety must be ensured by the implementation.
   *
   * @param value object to validate
   * @param context context in which the constraint is evaluated
   * @return {@code false} if {@code value} does not pass the constraint
   */
  @Override
  public boolean isValid(MinMaxDto value, ConstraintValidatorContext context) {

    // we don't include null check here because it should be validated with @NotNull
    return (value.getMin() == null || value.getMax() == null || value.getMin() < value.getMax());
  }
}
