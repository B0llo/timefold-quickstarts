package org.acme.vehiclerouting.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import org.acme.vehiclerouting.domain.Vehicle;
import org.acme.vehiclerouting.domain.Visit;
import org.acme.vehiclerouting.solver.justifications.MinimizeTravelTimeJustification;
import org.acme.vehiclerouting.solver.justifications.ServiceFinishedAfterMaxEndTimeJustification;
import org.acme.vehiclerouting.solver.justifications.VehicleCapacityJustification;
import org.acme.vehiclerouting.solver.justifications.VehicleLeftAfterMaxLastVisitDepartureTimeJustification;

public class VehicleRoutingConstraintProvider implements ConstraintProvider {

    public static final String VEHICLE_CAPACITY = "vehicleCapacity";
    public static final String SERVICE_FINISHED_AFTER_MAX_END_TIME = "serviceFinishedAfterMaxEndTime";
    public static final String MINIMIZE_TRAVEL_TIME = "minimizeTravelTime";

    public static final String VEHICLE_LEFT_AFTER_MAX_LAST_VISIT_DEPARTURE_TIME = "vehicleLeftAfterMaxLastVisitDepartureTime";

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                vehicleCapacity(factory),
                serviceFinishedAfterMaxEndTime(factory),
                vehicleLeavesAfterMaxLastVisitDepartureTime(factory),
                minimizeTravelTime(factory)
        };
    }

    // ************************************************************************
    // Hard constraints
    // ************************************************************************

    protected Constraint vehicleCapacity(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .filter(vehicle -> vehicle.getTotalDemand() > vehicle.getCapacity())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        vehicle -> vehicle.getTotalDemand() - vehicle.getCapacity())
                .justifyWith((vehicle, score) -> new VehicleCapacityJustification(vehicle.getId(), vehicle.getTotalDemand(),
                        vehicle.getCapacity()))
                .asConstraint(VEHICLE_CAPACITY);
    }

    protected Constraint serviceFinishedAfterMaxEndTime(ConstraintFactory factory) {
        return factory.forEach(Visit.class)
                .filter(Visit::isServiceFinishedAfterMaxEndTime)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        Visit::getServiceFinishedDelayInMinutes)
                .justifyWith((visit, score) -> new ServiceFinishedAfterMaxEndTimeJustification(visit.getId(),
                        visit.getServiceFinishedDelayInMinutes()))
                .asConstraint(SERVICE_FINISHED_AFTER_MAX_END_TIME);
    }

    /* TODO 2: WRITE CONSTRAINT
     *  This constraint has to be a hard constraint, because:
     *          "A human being CANNOT work without sleep."
     *  and
     *          "No vehicle is allowed to depart from its last visit later than the maxLastVisitDepartureTime"
     *
     */
    protected Constraint vehicleLeavesAfterMaxLastVisitDepartureTime(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .filter(Vehicle::didVehicleLeaveAfterMaxLastVisitDepartureTime)
                // TODO X: DETERMINE PENALTY
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        Vehicle::getLastVisitDepartureDelayInMinutes)
                .justifyWith((vehicle, score) -> new VehicleLeftAfterMaxLastVisitDepartureTimeJustification(vehicle.getId(),
                        vehicle.getLastVisitDepartureTime(),
                        vehicle.getMaxLastVisitDepartureTime()))
                .asConstraint(VEHICLE_LEFT_AFTER_MAX_LAST_VISIT_DEPARTURE_TIME);
    }


    // ************************************************************************
    // Soft constraints
    // ************************************************************************

    protected Constraint minimizeTravelTime(ConstraintFactory factory) {
        return factory.forEach(Vehicle.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        Vehicle::getTotalDrivingTimeSeconds)
                .justifyWith((vehicle, score) -> new MinimizeTravelTimeJustification(vehicle.getId(),
                        vehicle.getTotalDrivingTimeSeconds()))
                .asConstraint(MINIMIZE_TRAVEL_TIME);
    }
}
