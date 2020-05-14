package rmhub.mod.devicemgmt.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.exception.BusinessException;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.mod.devicemgmt.common.DeviceConst;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.dto.DevicePoolInfo;
import rmhub.mod.devicemgmt.dto.LocationInfo;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.dto.PoolInfo;
import rmhub.mod.devicemgmt.dto.ResponseDeviceInfo;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.repository.PhysicalDeviceRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class PhysicalDeviceServiceImplTest {

  @InjectMocks
  private PhysicalDeviceServiceImpl physicalDeviceService;

  @Mock
  private PhysicalDeviceRepo physicalDeviceRepo;

  @Mock
  private KafkaProducible<String, String> kafkaProducible;

  @BeforeEach
  void setupBefore() {
    ReflectionTestUtils.setField(physicalDeviceService, "requestDevice", "topic");
  }

  @Test
  void testGetDevices() {

    List<PhysicalDevice> dataList = new ArrayList<>();

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setDeploymentId(1L);
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    dataList.add(physicalDevice);

    when(physicalDeviceRepo.findAll(Mockito.any(Specification.class), Mockito.any(Sort.class))).thenReturn(dataList);

    List<PhysicalDeviceResponse> expected = new ArrayList<>();

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();
    physicalDeviceResponse.setId(1L);
    physicalDeviceResponse.setRegistration(DeviceConst.UNREGISTERED);
    physicalDeviceResponse.setStatus(StatusEnum.ACTIVE.getValue());

    expected.add(physicalDeviceResponse);

    List<PhysicalDeviceResponse> actual = physicalDeviceService.find(1L, 1L);

    assertEquals(expected, actual);
  }

  @Test
  void testGetByName_ACTIVE() {
    List<PhysicalDevice> dataList = new ArrayList<>();

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setDeploymentId(1L);
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());
    physicalDevice.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    dataList.add(physicalDevice);

    when(physicalDeviceRepo.findAll(Mockito.any(Specification.class), Mockito.any(Sort.class))).thenReturn(dataList);

    List<PhysicalDeviceResponse> expected = new ArrayList<>();

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();
    physicalDeviceResponse.setId(1L);
    physicalDeviceResponse.setRegistration(DeviceConst.UNREGISTERED);
    physicalDeviceResponse.setStatus(StatusEnum.ACTIVE.getValue());
    physicalDeviceResponse.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    expected.add(physicalDeviceResponse);

    List<PhysicalDeviceResponse> actual = physicalDeviceService.findByName(1L, 1L, "Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    assertEquals(expected, actual);
  }

  @Test
  void testGetByName_INACTIVE() {
    List<PhysicalDevice> dataList = new ArrayList<>();

    PhysicalDevice physicalDevice1 = new PhysicalDevice();
    physicalDevice1.setId(1L);
    physicalDevice1.setDeploymentId(1L);
    physicalDevice1.setStatus(StatusEnum.INACTIVE.getValue());
    physicalDevice1.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    dataList.add(physicalDevice1);

    when(physicalDeviceRepo.findAll(Mockito.any(Specification.class), Mockito.any(Sort.class))).thenReturn(dataList);

    List<PhysicalDeviceResponse> expected = new ArrayList<>();

    PhysicalDeviceResponse physicalDeviceResponse1 = new PhysicalDeviceResponse();
    physicalDeviceResponse1.setId(1L);
    physicalDeviceResponse1.setRegistration(DeviceConst.UNREGISTERED);
    physicalDeviceResponse1.setStatus(StatusEnum.INACTIVE.getValue());
    physicalDeviceResponse1.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    expected.add(physicalDeviceResponse1);

    List<PhysicalDeviceResponse> actual = physicalDeviceService.findByName(1L, 1L, "Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    assertEquals(expected, actual);
  }

  @Test
  void testDelete() {

    PhysicalDevice data = new PhysicalDevice();
    data.setId(1L);
    data.setDeploymentId(1L);
    data.setStatus(StatusEnum.ACTIVE.getValue());
    data.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    when(physicalDeviceRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(data));
    when(physicalDeviceRepo.saveAndFlush(Mockito.any())).thenReturn(data);

    physicalDeviceService.removeLocation(1L);

    verify(physicalDeviceRepo, times(1)).saveAndFlush(Mockito.any(PhysicalDevice.class));
  }

  @Test
  void testCreate() {
    PhysicalDevice data = new PhysicalDevice();
    data.setId(1L);
    data.setName("TC_40_400");
    data.setExternalId("TC_40_400");
    data.setStatus(1);

    when(physicalDeviceRepo.save(Mockito.any(PhysicalDevice.class))).thenReturn(data);

    PhysicalDevice expected = new PhysicalDevice();
    expected.setId(1L);
    expected.setName("TC_40_400");
    expected.setExternalId("TC_40_400");
    expected.setStatus(1);

    PhysicalDevice actual = physicalDeviceService.create(data);

    assertEquals(expected, actual);
  }

  @Test
  void testCreateOrUpdate() {
    PhysicalDevice data = new PhysicalDevice();
    data.setId(1L);
    data.setName("TC_40_400");
    data.setExternalId("TC_40_400");
    data.setStatus(1);

    when(physicalDeviceRepo.save(Mockito.any(PhysicalDevice.class))).thenReturn(data);

    PhysicalDevice expected = new PhysicalDevice();
    expected.setId(1L);
    expected.setName("TC_40_400");
    expected.setExternalId("TC_40_400");
    expected.setStatus(1);

    PhysicalDevice actual = physicalDeviceService.createOrUpdate(data);

    assertEquals(expected, actual);
  }

  @Test
  void testFindByDeviceType_PoolNull() {
    List<DevicePoolInfo> listData = new ArrayList<>();

    List<PoolInfo> poolInfos = new ArrayList<>();
    PoolInfo poolInfo = new PoolInfo();
    poolInfos.add(poolInfo);

    DevicePoolInfo data = new DevicePoolInfo();
    data.setId(1L);
    data.setExternalId("TC_40_400");
    data.setDeviceName("TC_40_400");
    data.setDeviceTypeId(1L);

    listData.add(data);

    when(physicalDeviceRepo.findAllDevicesByDeviceType(Mockito.anyLong(), Mockito.anyLong())).thenReturn(listData);

    List<ResponseDeviceInfo> expected = new ArrayList<>();

    ResponseDeviceInfo responseDeviceInfo = new ResponseDeviceInfo();
    responseDeviceInfo.setId(1L);
    responseDeviceInfo.setDeviceTypeId(1L);
    responseDeviceInfo.setExternalId("TC_40_400");
    responseDeviceInfo.setDeviceName("TC_40_400");
    responseDeviceInfo.setPools(poolInfos);

    expected.add(responseDeviceInfo);

    List<ResponseDeviceInfo> actual = physicalDeviceService.findByDeviceType(1L, 1L);

    assertEquals(expected, actual);
  }

  @Test
  void testFindByDeviceType_PoolNotNull() {
    List<DevicePoolInfo> listData = new ArrayList<>();

    List<PoolInfo> poolInfos = new ArrayList<>();

    PoolInfo poolInfo = new PoolInfo();
    poolInfos.add(poolInfo);

    PoolInfo poolInfo1 = new PoolInfo();
    poolInfos.add(poolInfo1);

    DevicePoolInfo data = new DevicePoolInfo();
    data.setId(1L);
    data.setExternalId("TC_40_400");
    data.setDeviceName("TC_40_400");
    data.setDeviceTypeId(1L);

    DevicePoolInfo data1 = new DevicePoolInfo();
    data1.setId(1L);
    data1.setExternalId("TC_40_400");
    data1.setDeviceName("TC_40_400");
    data1.setDeviceTypeId(1L);

    listData.add(data);
    listData.add(data1);

    when(physicalDeviceRepo.findAllDevicesByDeviceType(Mockito.anyLong(), Mockito.anyLong())).thenReturn(listData);

    List<ResponseDeviceInfo> expected = new ArrayList<>();

    ResponseDeviceInfo responseDeviceInfo = new ResponseDeviceInfo();
    responseDeviceInfo.setId(1L);
    responseDeviceInfo.setDeviceTypeId(1L);
    responseDeviceInfo.setExternalId("TC_40_400");
    responseDeviceInfo.setDeviceName("TC_40_400");
    responseDeviceInfo.setPools(poolInfos);

    expected.add(responseDeviceInfo);

    List<ResponseDeviceInfo> actual = physicalDeviceService.findByDeviceType(1L, 1L);

    assertEquals(expected, actual);
  }

  @Test
  void testFindByDeviceType_PoolNotNull_externalIdNull() {
    List<DevicePoolInfo> listData = new ArrayList<>();

    List<PoolInfo> poolInfos = new ArrayList<>();

    PoolInfo poolInfo = new PoolInfo();
    poolInfos.add(poolInfo);

    PoolInfo poolInfo1 = new PoolInfo();
    poolInfos.add(poolInfo1);

    DevicePoolInfo data = new DevicePoolInfo();
    data.setId(1L);
    data.setExternalId("TC_40_400");
    data.setDeviceName("TC_40_400");
    data.setDeviceTypeId(1L);

    DevicePoolInfo data1 = new DevicePoolInfo();
    data1.setId(1L);
    data1.setExternalId("TC_40_400");
    data1.setDeviceName("TC_40_400");
    data1.setDeviceTypeId(1L);

    DevicePoolInfo data2 = new DevicePoolInfo();
    data2.setId(1L);
    data2.setExternalId(null);
    data2.setDeviceName("TC_40_400");
    data2.setDeviceTypeId(1L);

    listData.add(data);
    listData.add(data1);
    listData.add(data2);

    when(physicalDeviceRepo.findAllDevicesByDeviceType(Mockito.anyLong(), Mockito.anyLong())).thenReturn(listData);

    List<ResponseDeviceInfo> expected = new ArrayList<>();

    ResponseDeviceInfo responseDeviceInfo = new ResponseDeviceInfo();
    responseDeviceInfo.setId(1L);
    responseDeviceInfo.setDeviceTypeId(1L);
    responseDeviceInfo.setExternalId("TC_40_400");
    responseDeviceInfo.setDeviceName("TC_40_400");
    responseDeviceInfo.setPools(poolInfos);

    ResponseDeviceInfo responseDeviceInfo1 = new ResponseDeviceInfo();
    responseDeviceInfo1.setId(1L);
    responseDeviceInfo1.setDeviceTypeId(1L);
    responseDeviceInfo1.setExternalId(null);
    responseDeviceInfo1.setDeviceName("TC_40_400");
    responseDeviceInfo1.setPools(Collections.singletonList(poolInfo));

    expected.add(responseDeviceInfo);
    expected.add(responseDeviceInfo1);

    List<ResponseDeviceInfo> actual = physicalDeviceService.findByDeviceType(1L, 1L);

    assertEquals(expected, actual);
  }

  @Test
  void testFindByDeploymentId() {
    List<DevicePoolInfo> listData = new ArrayList<>();

    List<PoolInfo> poolInfos = new ArrayList<>();
    PoolInfo poolInfo = new PoolInfo();
    poolInfos.add(poolInfo);

    DevicePoolInfo data = new DevicePoolInfo();
    data.setId(1L);
    data.setExternalId(null);
    data.setDeviceName("TC_40_400");
    data.setDeviceTypeId(1L);

    DevicePoolInfo data1 = new DevicePoolInfo();
    data1.setId(1L);
    data1.setExternalId("TC_40_400");
    data1.setDeviceName("TC_40_400");
    data1.setDeviceTypeId(1L);

    listData.add(data);
    listData.add(data1);

    when(physicalDeviceRepo.findAllDevicesByDeploymentId(Mockito.anyLong())).thenReturn(listData);

    List<ResponseDeviceInfo> expected = new ArrayList<>();

    ResponseDeviceInfo responseDeviceInfo = new ResponseDeviceInfo();
    responseDeviceInfo.setId(1L);
    responseDeviceInfo.setDeviceTypeId(1L);
    responseDeviceInfo.setExternalId(null);
    responseDeviceInfo.setDeviceName("TC_40_400");
    responseDeviceInfo.setPools(poolInfos);

    ResponseDeviceInfo responseDeviceInfo1 = new ResponseDeviceInfo();
    responseDeviceInfo1.setId(1L);
    responseDeviceInfo1.setDeviceTypeId(1L);
    responseDeviceInfo1.setExternalId("TC_40_400");
    responseDeviceInfo1.setDeviceName("TC_40_400");
    responseDeviceInfo1.setPools(poolInfos);

    expected.add(responseDeviceInfo);
    expected.add(responseDeviceInfo1);

    List<ResponseDeviceInfo> actual = physicalDeviceService.findByDeploymentId(1L);

    assertEquals(expected, actual);
  }

  @Test
  void testDeleteAll() {

    List<PhysicalDevice> physicalDeviceList = new ArrayList<>();

    PhysicalDevice data = new PhysicalDevice();
    data.setId(1L);
    data.setName("TC_40_400");
    data.setExternalId("TC_40_400");
    data.setStatus(StatusEnum.DELETE.getValue());

    PhysicalDevice data1 = new PhysicalDevice();
    data1.setId(1L);
    data1.setName("TC_40_400");
    data1.setExternalId("TC_40_400");
    data1.setStatus(StatusEnum.ACTIVE.getValue());

    physicalDeviceList.add(data);
    physicalDeviceList.add(data1);

    when(physicalDeviceRepo.findAllByDeviceType_id(Mockito.anyLong())).thenReturn(physicalDeviceList);

    PhysicalDevice dataSave = new PhysicalDevice();
    dataSave.setId(1L);
    dataSave.setName("TC_40_400");
    dataSave.setExternalId("TC_40_400");
    dataSave.setStatus(StatusEnum.DELETE.getValue());

    when(physicalDeviceRepo.saveAll(Mockito.anyList())).thenReturn(Collections.singletonList(dataSave));

    physicalDeviceService.deleteAll(1L);

    verify(physicalDeviceRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void testFindByExternalId() {
    PhysicalDevice expected = new PhysicalDevice();
    expected.setId(1L);
    expected.setName("TC_40_400");
    expected.setExternalId("TC_40_400");
    expected.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceRepo.findByExternalId(Mockito.anyString())).thenReturn(expected);

    PhysicalDevice actual = physicalDeviceService.findByExternalId(Mockito.anyString());

    assertEquals(expected, actual);
  }

  @Test
  void testFind_4_Argument() {
    List<PhysicalDevice> data = new ArrayList<>();

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setName("TC_40_400");
    physicalDevice.setExternalId("TC_40_400");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    data.add(physicalDevice);

    when(physicalDeviceRepo.findAll(Mockito.any(Example.class), Mockito.any(Sort.class))).thenReturn(data);

    List<PhysicalDeviceResponse> expected = new ArrayList<>();

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();
    physicalDeviceResponse.setId(1L);
    physicalDeviceResponse.setName("TC_40_400");
    physicalDeviceResponse.setExternal_id("TC_40_400");
    physicalDeviceResponse.setStatus(StatusEnum.ACTIVE.getValue());
    physicalDeviceResponse.setRegistration(DeviceConst.UNREGISTERED);

    expected.add(physicalDeviceResponse);

    List<PhysicalDeviceResponse> actual = physicalDeviceService.find(1L, "Device name", 1, 1L);

    assertEquals(expected, actual);
  }

  @Test
  void testFind_4_Argument_empty() {
    List<PhysicalDevice> data = new ArrayList<>();

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setName("TC_40_400");
    physicalDevice.setExternalId("TC_40_400");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    data.add(physicalDevice);

    when(physicalDeviceRepo.findAll(Mockito.any(Example.class), Mockito.any(Sort.class))).thenReturn(data);

    List<PhysicalDeviceResponse> expected = new ArrayList<>();

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();
    physicalDeviceResponse.setId(1L);
    physicalDeviceResponse.setName("TC_40_400");
    physicalDeviceResponse.setExternal_id("TC_40_400");
    physicalDeviceResponse.setStatus(StatusEnum.ACTIVE.getValue());
    physicalDeviceResponse.setRegistration(DeviceConst.UNREGISTERED);

    expected.add(physicalDeviceResponse);

    List<PhysicalDeviceResponse> actual = physicalDeviceService.find(1L, null, null, null);

    assertEquals(expected, actual);
  }

  @Test
  void setLocationTestOk() {

    LocationInfo locationInfo = new LocationInfo();
    locationInfo.setLatitude(123D);
    locationInfo.setLongitude(123D);

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDevice.setId(123L);
    physicalDevice.setLatitude(123D);
    physicalDevice.setLongitude(123D);

    when(physicalDeviceRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(physicalDevice));
    when(physicalDeviceRepo.saveAndFlush(Mockito.any(PhysicalDevice.class))).thenReturn(physicalDevice);

    PhysicalDeviceResponse expected = new PhysicalDeviceResponse();
    expected.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    expected.setId(123L);
    expected.setLatitude(123D);
    expected.setLongitude(123D);
    expected.setRegistration(DeviceConst.REGISTERED);

    PhysicalDeviceResponse actual = physicalDeviceService.setLocation(123L, locationInfo);

    assertEquals(expected, actual);
  }

  @Test
  void setLocationTestErrorInActive() {

    LocationInfo locationInfo = new LocationInfo();
    locationInfo.setLatitude(123D);
    locationInfo.setLongitude(123D);

    PhysicalDevice physicalDevice1 = new PhysicalDevice();
    physicalDevice1.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDevice1.setId(123L);
    physicalDevice1.setDeploymentId(1L);
    physicalDevice1.setExternalId("TC_96_050");
    physicalDevice1.setStatus(StatusEnum.INACTIVE.getValue());
    physicalDevice1.setIsRegistered(true);
    physicalDevice1.setLatitude(123D);
    physicalDevice1.setLongitude(123D);

    when(physicalDeviceRepo.findById(123L)).thenReturn(Optional.of(physicalDevice1));
    BusinessException exception = assertThrows(BusinessException.class,
        () -> physicalDeviceService.setLocation(123L, locationInfo));

    assertEquals(exception.getMessage(), "Device is INACTIVE.");
  }

  @Test
  void setLocationTestErrorEmpty() {

    LocationInfo locationInfo = new LocationInfo();
    locationInfo.setLatitude(123D);
    locationInfo.setLongitude(123D);

    PhysicalDevice physicalDevice1 = new PhysicalDevice();
    physicalDevice1.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDevice1.setId(123L);
    physicalDevice1.setDeploymentId(1L);
    physicalDevice1.setExternalId("TC_96_050");
    physicalDevice1.setStatus(StatusEnum.ACTIVE.getValue());
    physicalDevice1.setIsRegistered(true);
    physicalDevice1.setLatitude(123D);
    physicalDevice1.setLongitude(123D);

    when(physicalDeviceRepo.findById(123L)).thenReturn(Optional.empty());
    BusinessException exception = assertThrows(BusinessException.class,
        () -> physicalDeviceService.setLocation(123L, locationInfo));

    assertEquals(exception.getMessage(), "Device doesn't exist.");
  }

  @Test
  void findDeviceByIdOk() {

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDevice.setId(123L);
    physicalDevice.setExternalId("TC_96_050");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(physicalDevice));

    PhysicalDeviceResponse expected = new PhysicalDeviceResponse();
    expected.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    expected.setId(123L);
    expected.setExternal_id("TC_96_050");
    expected.setStatus(StatusEnum.ACTIVE.getValue());
    expected.setRegistration(DeviceConst.UNREGISTERED);

    PhysicalDeviceResponse actual = physicalDeviceService.findById(123L);

    assertEquals(expected, actual);
  }

  @Test
  void findDeviceByIdNotFound() {

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDevice.setId(123L);
    physicalDevice.setDeploymentId(1L);
    physicalDevice.setExternalId("TC_96_050");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceRepo.findById(123L)).thenReturn(Optional.empty());
    BusinessException exception = assertThrows(BusinessException.class, () -> physicalDeviceService.findById(123L));

    assertEquals(exception.getMessage(), "Device doesn't exist.");
  }

  @Test
  void sync_Weather() {
    physicalDeviceService.sync(1L, "requestId", 1L);

    verify(kafkaProducible, times(1)).send(Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void sync_Traffic() {
    physicalDeviceService.sync(2L, "requestId", 1L);

    verify(kafkaProducible, times(1)).send(Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void checkDuplicatedLocation_noDuplicatedFound_ok() {

    when(physicalDeviceRepo.getOne(anyLong())).thenReturn(PhysicalDevice.builder().deviceType(new DeviceType(1L)).build());

    when(physicalDeviceRepo.findByLatitudeAndLongitudeAndStatusNotAndDeviceType_Id(anyDouble(), anyDouble(), anyInt(), anyLong()))
        .thenReturn(Optional.empty());

    assertEquals(Optional.empty(),
        physicalDeviceService.checkDuplicatedLocation(1L, LocationInfo.builder().latitude(1D).longitude(1D).build()));
  }

  @Test
  void checkDuplicatedLocation_duplicatedFound_sameId_differentDeviceType_ok() {

    var dummyDevice = new PhysicalDevice();
    dummyDevice.setId(1L);

    when(physicalDeviceRepo.getOne(anyLong())).thenReturn(PhysicalDevice.builder().deviceType(new DeviceType(1L)).build());

    when(physicalDeviceRepo.findByLatitudeAndLongitudeAndStatusNotAndDeviceType_Id(anyDouble(), anyDouble(), anyInt(), anyLong()))
        .thenReturn(Optional.of(dummyDevice));

    assertEquals(Optional.empty(),
        physicalDeviceService.checkDuplicatedLocation(1L, LocationInfo.builder().latitude(1D).longitude(1D).build()));
  }

  @Test
  void checkDuplicatedLocation_duplicatedFound_differentId_notOk() {

    var dummyDeviceName = "Dummy";
    var dummyDevice = new PhysicalDevice();
    dummyDevice.setId(1L);
    dummyDevice.setName(dummyDeviceName);

    when(physicalDeviceRepo.getOne(anyLong())).thenReturn(PhysicalDevice.builder().deviceType(new DeviceType(1L)).build());

    when(physicalDeviceRepo.findByLatitudeAndLongitudeAndStatusNotAndDeviceType_Id(anyDouble(), anyDouble(), anyInt(), anyLong()))
        .thenReturn(Optional.of(dummyDevice));

    assertEquals(Optional.of(dummyDeviceName), physicalDeviceService.checkDuplicatedLocation(2L,
        LocationInfo.builder().latitude(1D).longitude(1D).build()));
  }
}
