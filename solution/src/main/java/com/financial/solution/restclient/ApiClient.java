package com.financial.solution.restclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financial.solution.configuration.ApiConfig;
import com.financial.solution.errorhandling.Error;
import com.financial.solution.model.Isin;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.financial.solution.errorhandling.ErrorType.INTERNAL_ERROR;
import static com.financial.solution.errorhandling.ErrorType.API_FAILURE;

@Repository
public class ApiClient {

    private final Logger LOG = LogManager.getLogger(ApiClient.class);

    private final ApiConfig apiConfig;
    private final RestTemplate apiRestTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ApiClient(ApiConfig apiConfig, RestTemplate apiRestTemplate) {
        this.apiConfig = apiConfig;
        this.apiRestTemplate = apiRestTemplate;
    }

    public Either<Error, String> getIsinInformation() {
        var builder = UriComponentsBuilder.fromHttpUrl(apiConfig.getUrl())
                .path("/api/v2/instruments");

        return Try.of(() -> apiRestTemplate.getForObject(builder.toUriString(), String.class))
                .toEither()
                .peekLeft(throwable -> LOG.error("API call failed with error {}. {} Stacktrace {}"
                        , throwable.getMessage(), printFailedError(builder.toUriString()), throwable.getStackTrace()))
                .mapLeft(throwable -> new Error(API_FAILURE, "API call failed with error- {}" + throwable.getMessage()));
    }

    private String printFailedError(Object request) {
        try {
            return ("API failure request-> " + objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Either<Error, Isin[]> getMockIsinInformation() {
        return Try.of(() -> objectMapper.readValue(getClass().getClassLoader().getResource("ApiResponse.json"), new TypeReference<Isin[]>() {
                }))
                .toEither()
                .peekLeft(throwable -> LOG.error("Failed to read mock response. Error Message : {} Stacktrace {}"
                        , throwable.getMessage(), throwable.getStackTrace()))
                .mapLeft(throwable -> new Error(INTERNAL_ERROR, throwable.getMessage()));
    }
}
