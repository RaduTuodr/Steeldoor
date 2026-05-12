package org.example.steeldoor.model.enums;

import lombok.Getter;

@Getter
public enum CompanySize {
    STARTUP(1), SMB(2), MID_MARKET(3), ENTERPRISE(4);

    private final int weight;
    CompanySize(int weight) { this.weight = weight; }
}
