package org.acme.vehiclerouting.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

public record AllowFloatingBreaksJustification(String vehicleName, boolean hadAbreak,
                                               String description) implements ConstraintJustification {
    public AllowFloatingBreaksJustification(String vehicleName, boolean hadAbreak) {

        this(vehicleName, hadAbreak, "Vehicle '%s' %s a break".formatted(vehicleName, hadAbreak ? "had" : "didn't have"));
    }
}
