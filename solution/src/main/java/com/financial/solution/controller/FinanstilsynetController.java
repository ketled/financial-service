package com.financial.solution.controller;

import com.financial.solution.errorhandling.ErrorResponse;
import com.financial.solution.service.FinanstilsynetService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.financial.solution.validator.RequestValidator.validation;

@RestController
@RequestMapping("/finance")
public class FinanstilsynetController {

    private final Logger LOG = LogManager.getLogger(FinanstilsynetController.class);
    private final FinanstilsynetService service;


    @Autowired
    public FinanstilsynetController(FinanstilsynetService service) {
        this.service = service;
    }

    @GetMapping("/info/{isin}")
    public ResponseEntity<?> getIsinInformation(
            @PathVariable("isin") String isin,
            @RequestParam(name = "fromDate") String fromDate,
            @RequestParam(name = "toDate") String toDate) {

        var validationErrorOrSuccess = validation(isin, fromDate, toDate);
        if (validationErrorOrSuccess.isInvalid())
            return ResponseEntity.badRequest().body(validationErrorOrSuccess.getError());

        return service.getIsinInformation(isin, fromDate, toDate)
        .fold(ErrorResponse::respondError,
                response -> ResponseEntity.ok().body(response));

    }
}
