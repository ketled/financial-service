package com.financial.solution.validator;

import com.financial.solution.common.Success;
import com.financial.solution.errorhandling.Error;
import io.vavr.control.Validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.financial.solution.errorhandling.ErrorType.VALIDATION_FAILED;
import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;

public class RequestValidator {

    private static final Success SUCCESSFUL = new Success();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Validation<Error, Success> validation(final String isin, final String fromDate, final String toDate) {
        if (isin.isEmpty())
            return invalid(new Error(VALIDATION_FAILED, "No isin information is provided."));
        if (!fromDate.isEmpty()) {
            try {
                sdf.parse(fromDate);
            } catch (ParseException e) {
                return invalid(new Error(VALIDATION_FAILED, "Wrong format is provided for from date."));
            }
        }
        if (!toDate.isEmpty()) {
            try {
                sdf.parse(toDate);
            } catch (ParseException e) {
                return invalid(new Error(VALIDATION_FAILED, "Wrong format is provided for to date."));
            }
        }
        return valid(SUCCESSFUL);
    }

}
