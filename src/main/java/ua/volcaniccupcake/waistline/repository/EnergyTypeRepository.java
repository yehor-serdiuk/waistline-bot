package ua.volcaniccupcake.waistline.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.volcaniccupcake.waistline.model.EnergyType;

@Repository
public interface EnergyTypeRepository extends CrudRepository<EnergyType, Integer> {
}
