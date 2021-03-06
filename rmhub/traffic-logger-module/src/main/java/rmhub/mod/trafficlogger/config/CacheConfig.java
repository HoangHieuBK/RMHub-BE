package rmhub.mod.trafficlogger.config;

import static rmhub.mod.trafficlogger.common.TrafficAlertConst.TRAFFIC_ALERT_CACHE_NAME;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@EnableCaching
@Configuration
public class CacheConfig {

  @Bean
  public CacheManager cacheManager() {
    return new ConcurrentMapCacheManager(TRAFFIC_ALERT_CACHE_NAME);
  }
}

