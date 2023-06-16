package ua.volcaniccupcake.waistline.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String name;
    private Double calories;
    private Double size;
    private Double fat;
    private Double carbs;
    private Double proteins;
    private Integer energyTypeId;
}
