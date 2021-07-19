package com.cloudburst.bjssbasket.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleOfferTest {


    @Test
    public void isValidForDateTests(){

        SimpleOffer offer = ImmutableSimpleOffer.builder()
                .targetItemCode("Apples")
                .label("Apples 10% off")
                .discountMultiplier(new BigDecimal(0.1d))
                .build();

        assertTrue("offer should be valid for any date but is not", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,1)));

        offer = ImmutableSimpleOffer.builder().from(offer)
                .validFrom(LocalDate.of(2017, Month.FEBRUARY,2))
                .validUpto(LocalDate.of(2017, Month.FEBRUARY,8))
                .build();

        assertTrue("offer should be valid but is not", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,2)));
        assertTrue("offer should be valid but is not", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,8)));
        assertTrue("offer should be valid but is not", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,5)));

        assertFalse("offer should NOT be valid but is", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,1)));
        assertFalse("offer should NOT be valid but is", offer.isValidForDate(LocalDate.of(2017, Month.FEBRUARY,9)));
    }

    @Test
    public void calculateDiscountTests(){

        SimpleOffer offer = ImmutableSimpleOffer.builder()
                .targetItemCode("Apples")
                .label("Apples 10% off")
                .discountMultiplier(new BigDecimal(0.1d))
                .build();

        Item item = Item.of("Apples","Apples",1d);

        BigDecimal discount = offer.calculateDiscount(item);

        assertEquals(new BigDecimal(-.1d),discount);
    }

}
