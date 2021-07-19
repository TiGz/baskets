package com.cloudburst.bjssbasket.model;


import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BasketTotalTest {

    @Test
    public void formatPriceTests(){

        assertEquals("-10p", BasketTotal.formatPrice(new BigDecimal(-.1d)));
        assertEquals("10p", BasketTotal.formatPrice(new BigDecimal(.1d)));
        assertEquals("£3.10", BasketTotal.formatPrice(new BigDecimal(3.1d)));
        assertEquals("£-3.10", BasketTotal.formatPrice(new BigDecimal(-3.1d)));

    }

    @Test
    public void formatTotalTest1(){

        List<Discount> discounts = new ArrayList<>();

        BasketTotal total = ImmutableBasketTotal.of(new BigDecimal(1.3d),discounts);

        String expected = "Subtotal: £1.30\n(No offers available)\nTotal price: £1.30";

        assertEquals(expected,total.formatTotal());

    }

    @Test
    public void formatTotalTest2(){

        List<Discount> discounts = new ArrayList<>();
        discounts.add(Discount.of("Apples 10% off", new BigDecimal(-0.1d)));

        BasketTotal total = ImmutableBasketTotal.of(new BigDecimal(3.1d),discounts);

        String expected = "Subtotal: £3.10\nApples 10% off: -10p\nTotal price: £3.00";

        assertEquals(expected,total.formatTotal());

    }

}
