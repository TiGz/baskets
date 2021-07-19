package com.cloudburst.bjssbasket.services;

import com.google.common.collect.Collections2;

import com.cloudburst.bjssbasket.model.Basket;
import com.cloudburst.bjssbasket.model.BasketTotal;
import com.cloudburst.bjssbasket.model.DiscountingContext;
import com.cloudburst.bjssbasket.model.ImmutableBasketTotal;
import com.cloudburst.bjssbasket.model.Item;
import com.cloudburst.bjssbasket.model.Offer;
import com.cloudburst.bjssbasket.repo.OfferRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that is responsible for pricing up a Basket of Items
 *
 * It will use the OfferRepository to look up the relevant offers
 * for the basket and then search for the best ordering of offers
 * to give the best price for the customer
 *
 */
public class BasketPricingService {

    private OfferRepository offerRepository;

    public BasketPricingService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }

    public BasketTotal findBestDiscounts(Basket basket){
        return findBestDiscounts(basket, LocalDate.now());
    }

    /**
     * Package visibility to aid testing with specific dates
     * @param basket
     * @param targetDate
     * @return the lowest basket total given the available offers
     */
    BasketTotal findBestDiscounts (Basket basket, LocalDate targetDate){

        List<Offer> potentialOffers = offerRepository.findValidOffersForDate(targetDate)
                .stream()
                .filter(o -> o.isValidForBasket(basket.getItems()))
                .collect(Collectors.toList());

        // let's put in a check as permutations is expensive
        // if we hit this in real world testing then we will
        // need to apply some heuristics to the potential offers
        if ( potentialOffers.size() > 12 ){
            throw new RuntimeException("Too many offers to compute!");
        }

        // let's order the items by price descending so we are most likely
        // to discount the most expensive items first
        List<Item> items = basket.getItems()
                .stream()
                .sorted((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()))
                .collect(Collectors.toList());

        // need all permutations of offers (thanks Guava! ;0)
        Collection<List<Offer>> offerPermutations = Collections2.permutations(potentialOffers);

        BigDecimal subTotal = basket.calculateSubTotal();

        List<BasketTotal> totals = new ArrayList<>();
        totals.add(calculateTotal(subTotal, items,new ArrayList<>()));
        for (List<Offer> offers : offerPermutations) {
            totals.add(calculateTotal(subTotal, items,offers));
        }

        return totals.stream().min((o1, o2) -> o1.calculateTotal().compareTo(o2.calculateTotal()))
                .orElseThrow(
                        () -> new RuntimeException("Failed to find min which should never occur!")
                );
    }

    private BasketTotal calculateTotal(BigDecimal subTotal, List<Item> items, Collection<Offer> offers) {
        DiscountingContext context = DiscountingContext.of(new ArrayList<>(),items);
        for (Offer offer : offers) {
            context = offer.apply(context);
        }
        return ImmutableBasketTotal.of(subTotal,context.getAppliedDiscounts());
    }

}
