package ee.blakcat.model;

import java.util.Collection;

public enum DeduplicateMode {
    REMOVE,
    COPY,
    INFO,
    ROLLBACK,
    REPORT;

    public static DeduplicateMode from(Collection<String> argSet) {
        if (argSet.contains("-remove")) {
            return REMOVE;
        } else if (argSet.contains("-copy")) {
            return COPY;
        } else if (argSet.contains("-info")) {
            return INFO;
        } else if (argSet.contains("-rollback")) {
            return ROLLBACK;
        } else if (argSet.contains("-report")) {
            return REPORT;
        } else {
            return INFO;
        }
    }
}
