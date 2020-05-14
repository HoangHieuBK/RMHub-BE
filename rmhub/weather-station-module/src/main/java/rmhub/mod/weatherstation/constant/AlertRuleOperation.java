package rmhub.mod.weatherstation.constant;

import java.util.Arrays;
import java.util.List;

public interface AlertRuleOperation {

  Integer GREATER = 1;
  Integer SMALLER = 2;
  Integer EQUALS = 3;
  List<Integer> lsOperation = Arrays.asList(GREATER, SMALLER, EQUALS);
}
