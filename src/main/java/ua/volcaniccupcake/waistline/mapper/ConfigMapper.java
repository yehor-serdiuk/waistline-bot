package ua.volcaniccupcake.waistline.mapper;

import ua.volcaniccupcake.waistline.model.Config;

public class ConfigMapper {
    public static String configToMessage(Config config) {
        return "Meal size:" + "\n"
                + config.getMealCalories() + " kcal" + "\n"
                + "\n"
                + "Energy ratio:" + "\n"
                + config.getEnergyRatio() + "\n"
                + "\n"
                + "Muscle ratio:" + "\n"
                + config.getMuscleRatio() + "\n"
                + "\n";
    }
}
