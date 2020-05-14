package rmhub.mod.weatherstation.constant;

import java.util.Arrays;
import java.util.List;

public interface AlertRuleLevel {

  Integer ONE = 1;
  Integer TWO = 2;
  Integer TREE = 3;
  Integer FOUR = 4;
  Integer FIVE = 5;
  List<Integer> lsLevel = Arrays.asList(ONE, TWO, TREE, FOUR, FIVE);
}
