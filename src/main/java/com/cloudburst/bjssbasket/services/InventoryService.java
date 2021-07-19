package com.cloudburst.bjssbasket.services;

import com.cloudburst.bjssbasket.model.ImmutableItem;
import com.cloudburst.bjssbasket.model.Item;
import com.cloudburst.bjssbasket.repo.ItemRepository;

import java.util.Optional;

/**
 * Factory for creating real items. Uses ItemRepository to provide prototype items
 */
public class InventoryService {

    private ItemRepository repository;

    public InventoryService (ItemRepository repository){
        this.repository = repository;
    }

    public Item getStock (String itemCode){
        Optional<Item> prototype = repository.findItemByCode(itemCode);
        if ( prototype.isPresent() ){
            return ImmutableItem.copyOf(prototype.get());
        }
        else{
            // @TODO: this is unchecked for now but we should re-visit
            throw new ItemNotFoundException("No Item available for code: " + itemCode);
        }

    }

    public class ItemNotFoundException extends RuntimeException {
        public ItemNotFoundException(String message) {
            super(message);
        }
    }
}
