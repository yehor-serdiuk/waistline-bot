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

    private Integer calories;

    @Builder.Default
    private Integer size = 100;

    @Builder.Default
    private Integer fat = 0;

    @Builder.Default
    private Integer carbs = 0;

    @Builder.Default
    private Integer proteins = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENERGY_TYPE_ID")
    private EnergyType energyType;
}
