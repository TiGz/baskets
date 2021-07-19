package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.math.BigDecimal;

/**
 * Encapsulate an Item in the Inventory and in the Basket
 *
 * Assume Pounds and Pence for now - kiss
 */
@Value.Immutable
@Serial.Structural
public abstract class Item {

    public abstract String getCode();

    public abstract String getLabel();

    public abstract BigDecimal getPrice();


    public static Item of (String code, String label, BigDecimal amount ){
        return ImmutableItem.builder()
                .code(code)
                .label(label)
                .price(amount)
                .build();
    }

    public static Item of (String code, String label, double amount ){
        return ImmutableItem.builder()
                .code(code)
                .label(label)
                .price(new BigDecimal(amount))
                .build();
    }
}
