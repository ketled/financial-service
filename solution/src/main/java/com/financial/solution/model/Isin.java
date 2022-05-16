package com.financial.solution.model;

import lombok.Data;

import java.util.List;

@Data
public class Isin {
    private String isin;
    private String issuerName;
    private List<Events> events;

}
