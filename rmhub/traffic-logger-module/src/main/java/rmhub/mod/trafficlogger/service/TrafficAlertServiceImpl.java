package rmhub.mod.trafficlogger.service;

import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertDto;
import rmhub.mod.trafficlogger.entity.TrafficAlert;
import rmhub.mod.trafficlogger.repository.TrafficAlertRepo;

@Service
class TrafficAlertServiceImpl implements TrafficAlertService {

  @Autowired
  private TrafficAlertRepo repository;

  @Autowired
  private ModelMapper modelMapper;

  // =======================================================================================================================================
  // For consumer and producer

  @Override
  public TrafficAlert create(TrafficAlert trafficAlert) {
    return repository.save(trafficAlert);
  }

  @Override
  public TrafficAlert update(TrafficAlert trafficAlert) {
    return repository.save(trafficAlert);
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public List<TrafficAlert> findAll() {
    return repository.findAll();
  }

  @Override
  public TrafficAlert findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Not found!"));
  }

  // =======================================================================================================================================
  // For controller

  @Override
  public List<TrafficAlertDto> list() {
    // use stream API to map all the entities into dtos
    return repository.findAll().stream().map(entity -> modelMapper.map(entity, TrafficAlertDto.class)).collect(Collectors.toList());
  }

  @Override
  public TrafficAlertDto get(Long id) {
    return modelMapper.map(findById(id), TrafficAlertDto.class);
  }

  @Override
  public TrafficAlertDto update(Long id, UpdateTrafficAlertDto dto) {

    var entity = findById(id);

    // map the dto into the entity
    modelMapper.map(dto, entity);

    // save the entity into DB
    var result = repository.save(entity);

    return modelMapper.map(result, TrafficAlertDto.class);
  }

}
