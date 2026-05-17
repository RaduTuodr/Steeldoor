package org.example.steeldoor.model.enums;

import lombok.Data;
import lombok.Getter;

@Getter
public enum RoundType {

    OA("OA"),
    BEHAVIORAL("Behavioral"),
    LIVE_CODING("Live Coding"),
    TECHNICAL_DISCUSSION("Technical Discussion"),
    SYSTEM_DESIGN("System Design"),
    HR("HR"),
    RECRUITER_CALL("Recruiter Call"),
    MANAGERIAL("Managerial");

    private final String dbValue;

    RoundType(String dbValue) {
        this.dbValue = dbValue;
    }

    public static RoundType fromDbValue(String dbValue) {
        if (dbValue == null) {
            return null;
        }
        for (RoundType type : values()) {
            if (type.dbValue.equals(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RoundType dbValue: " + dbValue);
    }
}
