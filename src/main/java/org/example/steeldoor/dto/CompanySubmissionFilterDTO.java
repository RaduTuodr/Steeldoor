package org.example.steeldoor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CompanySubmissionFilterDTO {
    private String query;
    private String position;
    private Boolean offerReceived;
    private Integer page;
    private Integer pageSize;
    private String sortDir;
    private String sortBy;
    private Integer userId;
}
