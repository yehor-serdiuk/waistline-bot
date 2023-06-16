package ua.volcaniccupcake.waistline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.volcaniccupcake.waistline.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findAllByNameContainingIgnoreCase(String name);
}
