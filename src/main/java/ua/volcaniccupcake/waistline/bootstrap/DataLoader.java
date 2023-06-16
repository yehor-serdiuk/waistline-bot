package ua.volcaniccupcake.waistline.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.volcaniccupcake.waistline.model.Config;
import ua.volcaniccupcake.waistline.model.EnergyType;
import ua.volcaniccupcake.waistline.model.Item;
import ua.volcaniccupcake.waistline.repository.ConfigRepository;
import ua.volcaniccupcake.waistline.repository.EnergyTypeRepository;
import ua.volcaniccupcake.waistline.repository.ItemRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ConfigRepository configRepository;
    private final ItemRepository itemRepository;
    private final EnergyTypeRepository energyTypeRepository;

    @Override
    public void run(String... args) {
        EnergyType energyTypeEnergy = energyTypeRepository.save(EnergyType.builder()
                .name("energy")
                .build());
        EnergyType energyTypeMuscle = energyTypeRepository.save(EnergyType.builder()
                .name("muscle")
                .build());

        configRepository.save(Config.builder()
                .mealCalories(500)
                .energyRatio(2)
                .muscleRatio(3)
                .build());

        itemRepository.save(Item.builder()
                .name("pasta")
                .calories(123.0)
                .energyType(energyTypeEnergy)
                .build());
        itemRepository.save(Item.builder()
                .name("steak")
                .calories(456.0)
                .energyType(energyTypeMuscle)
                .build());
    }
}
