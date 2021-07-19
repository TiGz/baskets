package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulate a basket of items
 */
@Value.Immutable
@Serial.Structural
public abstract class Basket {

    public abstract List<Item> getItems();

    public BigDecimal calculateSubTotal(){
        return getItems().stream()
                .map(i -> i.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Basket addItems(Item... items){
        return ImmutableBasket.builder()
                .from(this)
                .addItems(items)
                .build();
    }

    public static Basket of(Collection<Item> items){
        return ImmutableBasket.builder()
                .addAllItems(items)
                .build();
    }

}
