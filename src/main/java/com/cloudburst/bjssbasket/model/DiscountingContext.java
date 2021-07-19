package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.util.Collection;
import java.util.List;

/**
 * Used during the process of finding the optimal permutation
 * of discounts to apply to a specific basket
 */
@Value.Immutable
@Serial.Structural
public abstract class DiscountingContext {

    /**
     * The discounts that have been applied already
     * @return
     */
    public abstract List<Discount> getAppliedDiscounts();

    /**
     * Items that are still available to count as required items
     * @return
     */
    public abstract List<Item> getAvailableItems();


    public static DiscountingContext of (Collection<Discount> discounts, Collection<Item> availableItems){
        return ImmutableDiscountingContext.builder()
                .addAllAppliedDiscounts(discounts)
                .addAllAvailableItems(availableItems)
                .build();
    }

}
