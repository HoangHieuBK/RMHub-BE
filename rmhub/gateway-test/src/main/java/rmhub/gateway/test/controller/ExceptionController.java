package rmhub.gateway.test.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import rmhub.common.exception.ClientHttpStatusException;
import rmhub.common.exception.ServerHttpStatusException;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.util.PathUtil;
import rmhub.gateway.test.dto.ExceptionDto;
import rmhub.gateway.test.dto.ExceptionDto.ExceptionType;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

  /**
   * Verify the response when the exception is thrown inside RMHub system.
   */
  @PostMapping
  public ResponseEntity<?> throwException(@RequestBody ExceptionDto exceptionDto) {
    switch (exceptionDto.getType()) {
      case CLIENT:
        throw new ClientHttpStatusException(HttpStatus.valueOf(exceptionDto.getCode()), exceptionDto.getMessage(), null);
      case SERVER:
        throw new ServerHttpStatusException(HttpStatus.valueOf(exceptionDto.getCode()), exceptionDto.getMessage(), null);
      default:
        // we never fall to default block because of IllegalArgumentException
        return ResponseEntity.badRequest().body("Something wrong!");
    }
  }

  /**
   * When you want to specify all the necessary fields: status, message, data, path.
   */
  @GetMapping("/v2")
  public RmhubResponseEntity<List<ExceptionDto>> v2(WebRequest request) {

    // get the data
    var data = ExceptionDto.builder()
        .code(HttpStatus.I_AM_A_TEAPOT.value())
        .type(ExceptionType.CLIENT)
        .message("Exception for test!")
        .build();

    // build the response
    return RmhubResponseEntity.with(
        HttpStatus.OK,
        "v2",
        Collections.singletonList(data), // the data should be an array as designed
        PathUtil.getPath(request));
  }

  /**
   * When the status should be 200.
   */
  @GetMapping("/v3")
  public RmhubResponseEntity<List<ExceptionDto>> v3(WebRequest request) {

    // get the data
    var data = ExceptionDto.builder()
        .code(HttpStatus.I_AM_A_TEAPOT.value())
        .type(ExceptionType.CLIENT)
        .message("Exception for test!")
        .build();

    // build the response
    return RmhubResponseEntity.with(
        "v3",
        Collections.singletonList(data), // the data should be an array as designed
        PathUtil.getPath(request));
  }

  /**
   * When we don't need the data, for example, delete function.
   */
  @GetMapping("/v4")
  public RmhubResponseEntity<?> v4(WebRequest request) {

    // build the response
    return RmhubResponseEntity.with(
        "v4",
        PathUtil.getPath(request));
  }
}
