package rmhub.mod.trafficlogger.service;

import static rmhub.mod.trafficlogger.common.TrafficAlertConst.TRAFFIC_ALERT_CACHE_NAME;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.common.StatusEnum;
import rmhub.mod.trafficlogger.common.TrafficAlertConst;
import rmhub.mod.trafficlogger.dto.business.SpeedSettingDto;
import rmhub.mod.trafficlogger.dto.request.CreateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.entity.TrafficAlertSetting;
import rmhub.mod.trafficlogger.repository.TrafficAlertSettingRepo;

@Service
class TrafficAlertSettingServiceImpl implements TrafficAlertSettingService {

  @Autowired
  private TrafficAlertSettingRepo repository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  @CacheEvict(value = TRAFFIC_ALERT_CACHE_NAME, allEntries = true)
  public TrafficAlertSettingDto create(CreateTrafficAlertSettingDto dto) {

    checkCorrelationAndOverlap(dto.getMin(), dto.getMax());

    checkQuantity();

    // map the dto into an entity
    var entity = modelMapper.map(dto, TrafficAlertSetting.class);

    // set status to ACTIVE
    entity.setStatus(StatusEnum.ACTIVE);

    return modelMapper.map(repository.save(entity), TrafficAlertSettingDto.class);
  }

  // @CachePut annotation updates the cached value.
  @Override
  @CacheEvict(value = TRAFFIC_ALERT_CACHE_NAME, allEntries = true)
  public TrafficAlertSettingDto update(Long id, UpdateTrafficAlertSettingDto dto) {

    var entity = findIfPresent(id);

    checkCorrelationAndOverlap(dto.getMin(), dto.getMax(), id);

    // map the dto into the entity
    modelMapper.map(dto, entity);

    // save the entity into DB
    var result = repository.save(entity);

    return modelMapper.map(result, TrafficAlertSettingDto.class);
  }

  // @Cacheable annotation adds the caching behaviour.
  // If multiple requests are received, then the method won't be repeatedly executed, instead, the results are shared from cached storage.
  @Override
  @Cacheable(value = TRAFFIC_ALERT_CACHE_NAME)
  public List<TrafficAlertSettingDto> findAll() {

    // use stream API to map all the entities into dtos
    return repository.findAllByStatusNotOrderByCreatedDateAsc(StatusEnum.DELETED).stream()
        .map(entity -> modelMapper.map(entity, TrafficAlertSettingDto.class))
        .collect(Collectors.toList());
  }

  @Override
  public TrafficAlertSettingDto findById(Long id) {
    return modelMapper.map(findIfPresent(id), TrafficAlertSettingDto.class);
  }

  /**
   * Retrieve an entity by its id if it exists, otherwise throw an predefined exception.
   */
  private TrafficAlertSetting findIfPresent(Long id) {
    checkExistence(id);
    // we have checked the resource existence, so it's safe to get thr resource from optional
    return repository.findById(id).orElseThrow();
  }

  @Override
  @CacheEvict(value = TRAFFIC_ALERT_CACHE_NAME, allEntries = true)
  public void delete(Long id) {
    checkExistence(id);
    repository.setStatus(id, StatusEnum.DELETED);
  }

  /**
   * Check if Resource exists (logically), otherwise throw an predefined exception.
   */
  private void checkExistence(Long id) {

    if (!repository.existsByIdAndStatusNot(id, StatusEnum.DELETED)) {

      throw new BusinessException(ErrorCode.NOT_FOUND, "Alert Rule Not found!");
    }
  }

  private void checkQuantity() {
    if (repository.countByStatus(StatusEnum.ACTIVE) >= TrafficAlertConst.MAX_ALERT_RULE) {
      throw new BusinessException(ErrorCode.BAD_REQUEST, "Exceed maximum alerts: " + TrafficAlertConst.MAX_ALERT_RULE);
    }
  }

  /**
   * Correlation check between min and max speed in case of <b>creating</b>
   */
  private void checkCorrelationAndOverlap(Integer minSpeed, Integer maxSpeed) {

    checkCorrelationAndOverlap(minSpeed, maxSpeed, null);
  }

  /**
   * Correlation check between min and max speed for both <b>creating</b> and <b>updating</b> case.
   *
   * @param id if id is not null, it mean we are <b>updating</b>, so that it will be excluded in Overlap check.
   */
  private void checkCorrelationAndOverlap(Integer minSpeed, Integer maxSpeed, Long id) {

    // min speed must be less than max speed
    if (minSpeed >= maxSpeed) {
      throw new BusinessException(ErrorCode.BAD_REQUEST, "Min value can’t be greater than Max value.");
    }

    List<SpeedSettingDto> speedSettingList;

    if (id == null) {
      // in case CREATE
      speedSettingList = repository.findSpeedSettingByStatus(StatusEnum.ACTIVE);
    } else {
      // in case UPDATE
      speedSettingList = repository.findSpeedSettingByStatusAndIdNot(StatusEnum.ACTIVE, id);
    }

    if (speedSettingList.stream()
        .allMatch(speedSettingDto -> (minSpeed > speedSettingDto.getMax() || maxSpeed < speedSettingDto.getMin()))) {
      // this means the the specified range is not overlapped
      return;
    }

    throw new BusinessException(ErrorCode.BAD_REQUEST, "Alert values can’t overlap each other. Please change alert values.");
  }
}
