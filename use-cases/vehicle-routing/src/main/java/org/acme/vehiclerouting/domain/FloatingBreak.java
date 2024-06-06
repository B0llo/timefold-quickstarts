package org.acme.vehiclerouting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.time.LocalDateTime;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class FloatingBreak {
    private Duration duration;
    private LocalDateTime triggerTime;

    public FloatingBreak() {
    }

    public FloatingBreak(LocalDateTime triggerTime, Duration duration) {
        this.triggerTime = triggerTime;
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getTriggerTime() {
        return triggerTime;
    }

    public Duration doBrake() {
        return duration;
    }
}
