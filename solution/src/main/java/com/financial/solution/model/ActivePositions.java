package com.financial.solution.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ActivePositions {

    private String date;
    private BigDecimal shortPercent;
    private BigInteger shares;
    private String positionHolder;
}
