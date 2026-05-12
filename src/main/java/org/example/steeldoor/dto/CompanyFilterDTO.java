package org.example.steeldoor.dto;

import lombok.Data;

@Data
public class CompanyFilterDTO {
    private String query;
    private String industry;
    private String location;
    private String size;
    private String sortDir;
    private String sortBy;
}
