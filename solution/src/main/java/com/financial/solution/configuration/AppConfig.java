package com.financial.solution.configuration;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public ApiConfig apiConfig(@Value("${api.url}") String url) {
        return new ApiConfig(url);
    }

    @Bean
    public RestTemplate apiRestTemplate(@Value("${httpConnPool.maxTotal}") int maxTotal,
                                        @Value("${httpConnPool.defaultMaxPerRoute}") int defaultMaxPerRoute,
                                        @Value("${httpConnPool.timeout}") int timeout) {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient(maxTotal, defaultMaxPerRoute, timeout)));
    }

    @Bean
    public HttpHeaders apiHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private CloseableHttpClient httpClient(final int maxTotal, final int defaultMaxPerRoute, final int timeout) {
        final var poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(maxTotal);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);


        final var requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(timeout)
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();

        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
