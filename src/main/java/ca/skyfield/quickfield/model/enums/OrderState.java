package ca.skyfield.quickfield.model.enums;

public enum OrderState {
    TODO,
    IN_PROGRESS,
    CANCELLED,
    COMPLETED;

    public boolean canTransitionTo(OrderState targetState) {
        return switch (this) {
            case TODO -> targetState == IN_PROGRESS || targetState == CANCELLED;
            case IN_PROGRESS -> targetState == COMPLETED || targetState == CANCELLED;
            case COMPLETED,CANCELLED -> false;
        };
    }

}

