package com.api.task.constants;

import lombok.Getter;

public class Constants {
    public enum PRIORITY {
        LOW,
        MEDIUM,
        HIGH
    }

    @Getter
    public enum STATUS {
        UNASSIGNED("unassigned"),
        PENDING("pending"),
        COMPLETED("completed");
        private final String statusValue;

        STATUS(String s) {
            this.statusValue = s;
        }
    }
}
