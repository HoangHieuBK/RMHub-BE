package rmhub.mod.trafficlogger.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Custom Class-level validation for min-max correlation.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Constraint(validatedBy = MinMaxCorrelationValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinMaxCorrelation {

  // TODO will implement i18n later
  String message() default "Min value can’t be greater than Max value!";

  String min();

  String max();

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
