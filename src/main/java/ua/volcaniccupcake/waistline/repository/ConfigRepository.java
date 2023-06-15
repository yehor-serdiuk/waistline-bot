package ua.volcaniccupcake.waistline.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.volcaniccupcake.waistline.model.Config;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Integer> {
}
