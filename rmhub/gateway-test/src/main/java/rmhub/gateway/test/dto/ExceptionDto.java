package rmhub.gateway.test.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Data
public class ExceptionDto {

  private ExceptionType type;
  private Integer code;
  private String message;

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public enum ExceptionType {
    CLIENT("client"),
    SERVER("server");

    @JsonValue
    @Getter
    private final String type;
  }
}
