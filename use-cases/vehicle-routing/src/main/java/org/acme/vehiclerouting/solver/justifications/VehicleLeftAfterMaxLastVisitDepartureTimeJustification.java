package org.acme.vehiclerouting.solver.justifications;

import ai.timefold.solver.core.api.score.stream.ConstraintJustification;

import java.time.LocalDateTime;

public record VehicleLeftAfterMaxLastVisitDepartureTimeJustification(String visitId, LocalDateTime departureTime,
                                                                     LocalDateTime maxLastVisitDepartureTime,
                                                                     String description) implements ConstraintJustification {
    public VehicleLeftAfterMaxLastVisitDepartureTimeJustification(String vehicleId, LocalDateTime departureTime, LocalDateTime maxLastVisitDepartureTime) {
        this(vehicleId, departureTime, maxLastVisitDepartureTime, "Vehicle '%s' left after its max last visit departure time (%s) at (%s)."
                .formatted(vehicleId, departureTime, maxLastVisitDepartureTime));
    }

}
