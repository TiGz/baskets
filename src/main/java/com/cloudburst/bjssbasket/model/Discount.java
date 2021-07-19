package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.math.BigDecimal;

/**
 * Encapsulate an applied offer resulting in a reduction in total price for the basket
 */
@Value.Immutable
@Serial.Structural
public abstract class Discount implements Comparable<Discount>{

    @Value.Parameter
    public abstract String getLabel();

    @Value.Parameter
    public abstract BigDecimal getAmount();

    public static Discount of (String label, BigDecimal amount){
        return ImmutableDiscount.of(label,amount);
    }

    @Override
    public int compareTo(Discount o) {
        return this.getLabel().compareTo(o.getLabel());
    }
}
