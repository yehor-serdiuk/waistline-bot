package ua.volcaniccupcake.waistline.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.exception.ItemNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StringUtil {

    public String convertItemInputToJson(String input) {
        // Variables to hold our parsed data
        String name = "";
        int calories = 0;
        int size = 0;
        int fat = 0;
        int carbs = 0;
        int proteins = 0;
        String energyTypeName = "";

        // Split the input string by newlines
        String[] lines = input.split("\\r?\\n");

        for (String line : lines) {
            // Ensure the line actually contains a key-value pair
            if (!line.contains(":")) continue;

            // Split by the first colon only
            String[] parts = line.split(":", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (key) {
                case "Name":
                    name = value;
                    break;
                case "Calories":
                    calories = parseNumericValue(value);
                    break;
                case "Size":
                    size = parseNumericValue(value); // Automatically strips the '*'
                    break;
                case "Fat":
                    fat = parseNumericValue(value);
                    break;
                case "Carbs":
                    carbs = parseNumericValue(value);
                    break;
                case "Proteins":
                    proteins = parseNumericValue(value);
                    break;
                case "Energy Type Name":
                    energyTypeName = value;
                    break;
            }
        }

        // Construct the JSON string manually using a template
        return String.format(
                "{\n" +
                        "   \"name\":\"%s\",\n" +
                        "   \"calories\":%d,\n" +
                        "   \"size\":%d,\n" +
                        "   \"fat\":%d,\n" +
                        "   \"carbs\":%d,\n" +
                        "   \"proteins\":%d,\n" +
                        "   \"energyTypeName\":\"%s\"\n" +
                        "}",
                name, calories, size, fat, carbs, proteins, energyTypeName
        );
    }

    // Helper method to strip out non-digit characters (like '*') and parse safely
    private int parseNumericValue(String value) {
        String cleanValue = value.replaceAll("[^0-9]", "");
        return cleanValue.isEmpty() ? 0 : Integer.parseInt(cleanValue);
    }

}
