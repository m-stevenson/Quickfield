package ca.skyfield.quickfield.model.enums;

public enum OrderState {
    NEW,
    ASSIGNED,
    IN_PROGRESS,
    PAUSED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(OrderState targetState) {
        return switch (this) {
            case NEW -> targetState == ASSIGNED || targetState == CANCELLED;
            case ASSIGNED -> targetState == IN_PROGRESS || targetState == PAUSED ||targetState == CANCELLED;
            case IN_PROGRESS -> targetState == COMPLETED || targetState == PAUSED || targetState == CANCELLED;
            case PAUSED -> targetState == IN_PROGRESS || targetState == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }

}

