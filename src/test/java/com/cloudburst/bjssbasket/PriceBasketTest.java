package com.cloudburst.bjssbasket;


import com.cloudburst.bjssbasket.model.BasketTotal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PriceBasketTest {


    @Test
    public void applesMilkBreadTest() {

        PriceBasket pb = new PriceBasket();
        pb.addItem("Apples");
        pb.addItem("Milk");
        pb.addItem("Bread");

        BasketTotal total = pb.calculateTotalPrice();

        String expected = "Subtotal: £3.10\nApples 10% off: -10p\nTotal price: £3.00";

        assertEquals(expected,total.formatTotal());
    }

    @Test
    public void justMilkTest() {

        PriceBasket pb = new PriceBasket();
        pb.addItem("Milk");

        BasketTotal total = pb.calculateTotalPrice();

        String expected = "Subtotal: £1.30\n(No offers available)\nTotal price: £1.30";

        assertEquals(expected,total.formatTotal());


    }


}
