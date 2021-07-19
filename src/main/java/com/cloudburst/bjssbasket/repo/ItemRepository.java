package com.cloudburst.bjssbasket.repo;

import com.cloudburst.bjssbasket.model.Item;

import java.util.Optional;

/**
 * Encapsulate a data store of Item prototypes
 *
 */
public interface ItemRepository {

    /**
     * Lookup the item corresponding to the given code in the repo
     * @param itemCode
     * @return an optional containing the item prototype or nullable if none found
     */
    Optional<Item> findItemByCode(String itemCode);

}
