package com.cloudburst.bjssbasket.repo;

import com.cloudburst.bjssbasket.model.ImmutableSimpleOffer;
import com.cloudburst.bjssbasket.model.Offer;
import com.cloudburst.bjssbasket.model.SimpleOffer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * In memory hard coded offer repo to get us going
 *
 * Consider replacing with a more suitable back end soon
 */
public class HardCodedOfferRepository implements OfferRepository {

    private List<SimpleOffer> offers = new Vector<>();

    public HardCodedOfferRepository (){

        // Apples have a 10% discount off their normal price this week
        offers.add(ImmutableSimpleOffer.builder()
                .label("Apples 10% off")
                .targetItemCode("Apples")
                .discountMultiplier(new BigDecimal(0.1d))
                .validFrom(LocalDate.of(2017, Month.FEBRUARY,2))
                .validUpto(LocalDate.of(2017, Month.FEBRUARY,8))
                .build()
        );

        // Buy 2 tins of soup and get a loaf of bread for half price
        offers.add(ImmutableSimpleOffer.builder()
                .label("2x Soup so Bread 50% off")
                .targetItemCode("Bread")
                .discountMultiplier(new BigDecimal(0.5d))
                .requiredItemCode("Soup")
                .requiredItemCount(2)
                .build()
        );

    }

    @Override
    public List<Offer> findValidOffersForDate(LocalDate targetDate) {
        return offers.stream()
                .filter(o -> o.isValidForDate(targetDate))
                .collect(Collectors.toList());
    }


}
