package org.acme.vehiclerouting.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class FloatingBreak {
    private boolean isOver;
    private final Duration duration;

    private final LocalDateTime triggerTime;

    public FloatingBreak(LocalDateTime triggerTime, Duration duration) {
        this.isOver = false;
        this.triggerTime = triggerTime;
        this.duration = duration;
    }

    public boolean isOver() {
        return isOver;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public Duration doBrake() {
        isOver = true;
        return duration;
    }
}
