package com.cloudburst.bjssbasket.repo;

import com.cloudburst.bjssbasket.model.Item;

import java.util.Collection;
import java.util.Optional;
import java.util.Vector;

/**
 * In memory hard coded repo will get us going (kiss)
 *
 * In the future we should consider adding a database back end
 * or even just a JSON file to store our prototype items
 *
 */
public class HardCodedItemRepository implements ItemRepository {

    private Collection<Item> items = new Vector<>();

    public HardCodedItemRepository(){
        items.add(Item.of("Soup","Soup",0.65d));
        items.add(Item.of("Bread","Bread",0.8d));
        items.add(Item.of("Milk","Milk",1.3d));
        items.add(Item.of("Apples","Apples",1d));
    }

    @Override
    public Optional<Item> findItemByCode(String itemCode) {
        return items.stream()
                .filter(i -> i.getCode().equals(itemCode))
                .findFirst();
    }
}
