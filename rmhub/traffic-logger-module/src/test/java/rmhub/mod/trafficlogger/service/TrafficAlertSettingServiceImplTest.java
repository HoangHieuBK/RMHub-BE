package rmhub.mod.trafficlogger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.common.StatusEnum;
import rmhub.mod.trafficlogger.common.TrafficAlertConst;
import rmhub.mod.trafficlogger.dto.business.SpeedSettingDto;
import rmhub.mod.trafficlogger.dto.request.CreateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.entity.TrafficAlertSetting;
import rmhub.mod.trafficlogger.repository.TrafficAlertSettingRepo;

@ExtendWith(SpringExtension.class)
class TrafficAlertSettingServiceImplTest {

  private ModelMapper modelMapper = new ModelMapper();

  @Mock
  private ModelMapper mockModelMapper;

  @Mock
  private TrafficAlertSettingRepo repository;

  @InjectMocks
  private TrafficAlertSettingServiceImpl serviceImpl;

  private final TrafficAlertSetting trafficAlertSetting = TrafficAlertSetting.builder()
      .id(1L)
      .level(1)
      .color("red")
      .description("123")
      .min(1)
      .max(100)
      .status(StatusEnum.ACTIVE)
      .createdDate(new Date())
      .lastModifiedDate(new Date())
      .build();

  private final TrafficAlertSettingDto trafficAlertSettingDto = TrafficAlertSettingDto.builder()
      .id(1L)
      .level(1)
      .color("red")
      .description("123")
      .min(1)
      .max(100)
      .createdDate(new Date())
      .lastModifiedDate(new Date())
      .build();

  // =======================================================================================================================================
  // create

  @Test
  void create_trafficAlertSetting_Ok() {
    // given
    var requestDto = CreateTrafficAlertSettingDto.builder().level(5).min(1).max(2).build();

    var expectedDto = modelMapper.map(requestDto, TrafficAlertSettingDto.class);
    expectedDto.setId(1L);

    var expectedEntity = modelMapper.map(expectedDto, TrafficAlertSetting.class);

    // when
    when(mockModelMapper.map(ArgumentMatchers.any(CreateTrafficAlertSettingDto.class), ArgumentMatchers.any()))
    .thenReturn(expectedEntity);
    when(mockModelMapper.map(ArgumentMatchers.any(TrafficAlertSetting.class), ArgumentMatchers.any())).thenReturn(expectedDto);
    when(repository.save(ArgumentMatchers.any(TrafficAlertSetting.class))).thenReturn(expectedEntity);
    mockCheckCorrelationAndOverlap(true);

    // then
    var result = serviceImpl.create(requestDto);
    assertEquals(expectedDto, result);
  }

  @Test
  void create_trafficAlertSetting_quantity_shouldFail() {

    CreateTrafficAlertSettingDto createTrafficAlertSettingDto = CreateTrafficAlertSettingDto.builder()
        .level(1)
        .color("red")
        .description("123")
        .min(1)
        .max(100)
        .build();

    mockCheckQuantity(4);

    BusinessException exception = assertThrows(BusinessException.class, () -> serviceImpl.create(createTrafficAlertSettingDto));
    Assert.assertEquals(exception.getMessage(), "Exceed maximum alerts: " + TrafficAlertConst.MAX_ALERT_RULE);
  }

  // =======================================================================================================================================
  // update

  @Test
  void update_trafficAlertSettingDto_OK() {

    UpdateTrafficAlertSettingDto updateTrafficAlertSettingDto = UpdateTrafficAlertSettingDto.builder()
        .level(1)
        .color("red")
        .description("123")
        .min(20)
        .max(30)
        .build();

    mockFindIfPresent();
    mockCheckCorrelationAndOverlap(false);
    when(repository.save(Mockito.any(TrafficAlertSetting.class))).thenReturn(trafficAlertSetting);
    when(mockModelMapper.map(Mockito.any(TrafficAlertSetting.class), Mockito.any())).thenReturn(trafficAlertSettingDto);

    var actual = serviceImpl.update(1L, updateTrafficAlertSettingDto);
    Assert.assertEquals(trafficAlertSettingDto, actual);
  }

  @Test
  void update_trafficAlertSettingDto_minGreaterThanMax_shouldFail() {

    UpdateTrafficAlertSettingDto shouldBeFailed = UpdateTrafficAlertSettingDto.builder()
        .level(1)
        .color("red")
        .description("123")
        .min(100)
        .max(10)
        .build();

    mockFindIfPresent();
    mockCheckCorrelationAndOverlap(false);

    BusinessException exception = assertThrows(BusinessException.class, () -> serviceImpl.update(1L, shouldBeFailed));
    Assert.assertEquals(exception.getMessage(), "Min value can’t be greater than Max value.");
  }

  @Test
  void update_trafficAlertSettingDto_overlap_shouldFail() {

    UpdateTrafficAlertSettingDto shouldBeFailed = UpdateTrafficAlertSettingDto.builder()
        .level(1)
        .color("red")
        .description("123")
        .min(1)
        .max(50)
        .build();

    mockFindIfPresent();
    mockCheckCorrelationAndOverlap(false);

    BusinessException exception = assertThrows(BusinessException.class, () -> serviceImpl.update(1L, shouldBeFailed));
    Assert.assertEquals(exception.getMessage(), "Alert values can’t overlap each other. Please change alert values.");
  }

  // =======================================================================================================================================
  // delete

  @Test
  void delete_trafficAlertSetting_OK() {

    mockCheckExistence();

    serviceImpl.delete(1L);
    Mockito.verify(repository, Mockito.times(1)).setStatus(Mockito.anyLong(), Mockito.any(StatusEnum.class));
  }

  @Test
  void delete_trafficAlertSetting_notFound_shouldFail() {

    when(repository.existsByIdAndStatusNot(anyLong(), any(StatusEnum.class))).thenReturn(false);

    BusinessException exception = assertThrows(BusinessException.class, () -> serviceImpl.delete(56L));
    Assert.assertEquals(exception.getMessage(), "Alert Rule Not found!");
  }

  // =======================================================================================================================================
  // findById

  @Test
  void findById_OK() {

    mockFindIfPresent();
    when(mockModelMapper.map(Mockito.any(TrafficAlertSetting.class), Mockito.any())).thenReturn(trafficAlertSettingDto);

    TrafficAlertSettingDto actual = serviceImpl.findById(1L);
    assertEquals(trafficAlertSettingDto, actual);
  }

  // =======================================================================================================================================
  // findAll

  @Test
  void findAll_OK() {

    List<TrafficAlertSetting> list = new ArrayList<>();
    TrafficAlertSetting trafficAlertSetting1 = TrafficAlertSetting.builder()
        .id(2L)
        .level(1)
        .color("red")
        .description("123")
        .min(1)
        .max(100)
        .createdDate(new Date())
        .lastModifiedDate(new Date())
        .build();

    TrafficAlertSetting trafficAlertSetting2 = TrafficAlertSetting.builder()
        .id(3L)
        .level(1)
        .color("red")
        .description("123")
        .min(1)
        .max(100)
        .createdDate(new Date())
        .lastModifiedDate(new Date())
        .build();

    list.add(trafficAlertSetting);
    list.add(trafficAlertSetting1);
    list.add(trafficAlertSetting2);

    when(repository.findAllByStatusNotOrderByCreatedDateAsc(Mockito.any(StatusEnum.class))).thenReturn(list);
    when(mockModelMapper.map(Mockito.any(TrafficAlertSetting.class), any())).thenReturn(trafficAlertSettingDto);

    Assertions.assertEquals(3, serviceImpl.findAll().size());

    verify(mockModelMapper, Mockito.times(3)).map(Mockito.any(TrafficAlertSetting.class), any());
    verify(repository, Mockito.times(1)).findAllByStatusNotOrderByCreatedDateAsc(Mockito.any(StatusEnum.class));
  }

  // =======================================================================================================================================
  // helpful mock methods

  /**
   * existence check always return <code>true</code>.
   */
  private void mockCheckExistence() {
    when(repository.existsByIdAndStatusNot(anyLong(), any(StatusEnum.class))).thenReturn(true);
  }

  /**
   * <code>findIfPresent</code> always return a record.
   */
  private void mockFindIfPresent() {
    mockCheckExistence();
    when(repository.findById(anyLong())).thenReturn(Optional.of(trafficAlertSetting));
  }

  /**
   * quantity check always return passed quantity.
   */
  private void mockCheckQuantity(int quantity) {
    when(repository.countByStatus(Mockito.any(StatusEnum.class))).thenReturn(quantity);
  }

  /**
   * For ease of use, the <code>SpeedSettingDto</code> list always return a record with min = 5 and max = 10.
   */
  private void mockCheckCorrelationAndOverlap(boolean forCreate) {

    SpeedSettingDto speedSettingDto = SpeedSettingDto.builder().min(5).max(10).build();
    List<SpeedSettingDto> expected = Collections.singletonList(speedSettingDto);

    if (forCreate) {
      when(repository.findSpeedSettingByStatus(any(StatusEnum.class))).thenReturn(expected);
    } else {
      when(repository.findSpeedSettingByStatusAndIdNot(any(StatusEnum.class), anyLong())).thenReturn(expected);
    }
  }
}
