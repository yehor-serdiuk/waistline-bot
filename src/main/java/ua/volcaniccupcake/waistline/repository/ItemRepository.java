package ua.volcaniccupcake.waistline.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.volcaniccupcake.waistline.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
}
