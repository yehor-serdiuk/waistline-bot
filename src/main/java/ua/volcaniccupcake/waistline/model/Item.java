package ua.volcaniccupcake.waistline.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double calories;

    @Builder.Default
    private Double size = 100.0;

    @Builder.Default
    private Double fat = 0.0;

    @Builder.Default
    private Double carbs = 0.0;

    @Builder.Default
    private Double proteins = 0.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENERGY_TYPE_ID")
    private EnergyType energyType;
}
