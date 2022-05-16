package com.financial.solution.service;

import com.financial.solution.errorhandling.Error;
import com.financial.solution.model.Events;
import com.financial.solution.model.Isin;
import com.financial.solution.restclient.ApiClient;
import io.vavr.control.Either;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.financial.solution.errorhandling.ErrorType.INVALID_INPUT;
import static java.util.Objects.isNull;

@Service
public class FinanstilsynetService {

    private final ApiClient apiClient;
    private final Logger LOG = LogManager.getLogger(FinanstilsynetService.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    public FinanstilsynetService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public Either<Error, List<Isin>> getIsinInformation(String isin, String fromDate, String toDate) {
        LOG.info("Information is request for isin {} from date {} to date{}", isin, fromDate, toDate);
        /*
        Call from api client is commented,
        as connection time out is coming from the end point,
        may be because I'm running behind proxy.
        Hardcoded end point response to proceed further with the task.

        var apiResponse = apiClient.getIsinInformation();
        */
        var apiResponse = apiClient.getMockIsinInformation();

        if (apiResponse.isLeft())
            return Either.left(apiResponse.getLeft());
        else
            return queryIsin(isin, fromDate, toDate, apiResponse.get());
    }

    private Either<Error, List<Isin>> queryIsin(String queryIsin, String fromDate, String toDate, Isin[] isinList) {
        List<Isin> financeDetails = Arrays.stream(isinList).filter(isin -> isin.getIsin().equalsIgnoreCase(queryIsin)).collect(Collectors.toList());
        if (financeDetails.size() > 0 && !(fromDate.isEmpty() && toDate.isEmpty())) {
            List<Isin> financeDetailsWithDate = new ArrayList<>();
            try {
                Date startDate = sdf.parse(fromDate);
                Date endDate = sdf.parse(toDate);
                financeDetails.forEach(isin -> isin.getEvents().forEach(events -> {
                    Date eventDate = null;
                    List<Events> eveList = new ArrayList<>();
                    try {
                        eventDate = sdf.parse(events.getDate());
                    } catch (ParseException e) {
                        LOG.error("Error occurred in parsing date {}", e.getMessage());
                    }
                    if (!isNull(eventDate) && eventDate.after(startDate) && eventDate.before(endDate)) {
                        Isin in = new Isin();
                        in.setIsin(isin.getIsin());
                        in.setIssuerName(isin.getIssuerName());
                        eveList.add(events);
                        in.setEvents(eveList);
                        financeDetailsWithDate.add(in);
                    }
                }));
            } catch (ParseException e) {
                LOG.error("Error occurred in parsing date {}", e.getMessage());
                return Either.left(new Error(INVALID_INPUT, e.getMessage()));
            }
            return Either.right(financeDetailsWithDate);
        } else
            return Either.right(financeDetails);
    }
}
