package ua.volcaniccupcake.waistline.exception;

import ua.volcaniccupcake.waistline.model.EnergyType;

public class EnergyTypeNotFoundException extends RuntimeException {
    public EnergyTypeNotFoundException(String message) {
        super(message);
    }
}
