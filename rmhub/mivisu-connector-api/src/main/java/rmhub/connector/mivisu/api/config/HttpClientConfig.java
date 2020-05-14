package rmhub.connector.mivisu.api.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * - Supports both HTTP and HTTPS - Uses a connection pool to re-use connections and save overhead of creating connections. - Has a custom
 * connection keep-alive strategy (to apply a default keep-alive if one isn't specified) - Starts an idle connection monitor to continuously
 * clean up stale connections.
 */
@Slf4j
@Configuration
public class HttpClientConfig {

  // Determines the timeout in milliseconds until a connection is established.
  private static final int CONNECT_TIMEOUT = 30000;

  // The timeout when requesting a connection from the connection manager.
  private static final int REQUEST_TIMEOUT = 30000;

  // The timeout for waiting for data
  private static final int SOCKET_TIMEOUT = 60000;

  private static final int MAX_TOTAL_CONNECTIONS = 50;

  private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;

  @Bean
  public PoolingHttpClientConnectionManager poolingConnectionManager() {
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
    } catch (NoSuchAlgorithmException | KeyStoreException e) {
      log.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(),
          e);
    }

    SSLConnectionSocketFactory sslsf = null;
    try {
      sslsf = new SSLConnectionSocketFactory(builder.build());
    } catch (KeyManagementException | NoSuchAlgorithmException e) {
      log.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage(),
          e);
    }

    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslsf)
            .register("http", new PlainConnectionSocketFactory()).build();

    PoolingHttpClientConnectionManager poolingConnectionManager =
        new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    return poolingConnectionManager;
  }

  @Bean
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
    return (response, context) -> {
      HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
      while (it.hasNext()) {
        HeaderElement he = it.nextElement();
        String param = he.getName();
        String value = he.getValue();

        if (value != null && param.equalsIgnoreCase("timeout")) {
          return Long.parseLong(value) * 1000;
        }
      }
      return DEFAULT_KEEP_ALIVE_TIME_MILLIS;
    };
  }

  @Bean
  public CloseableHttpClient httpClient() {
    RequestConfig requestConfig =
        RequestConfig.custom().setConnectionRequestTimeout(REQUEST_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();

    return HttpClients.custom().setDefaultRequestConfig(requestConfig)
        .setConnectionManager(poolingConnectionManager())
        .setKeepAliveStrategy(connectionKeepAliveStrategy()).build();
  }

}
