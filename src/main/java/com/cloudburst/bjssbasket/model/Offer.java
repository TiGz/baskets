package com.cloudburst.bjssbasket.model;

import java.time.LocalDate;
import java.util.Collection;

/**
 * We don't know what kind of offers we want to support so
 * we will try to keep offer agnostic of the mechanics of the discounting
 * so we will use a visitor style pattern whereby the current discounting
 * context is passed into the offer which is then responsible for replacing
 * the context if the offer is successfully applied
 *
 * We have implemented SimpleOffer to cover off the initial use cases
 * but me might want to add other offers like Meal Deal etc
 *
 */
public interface Offer {

    /**
     * Apply this offer to the given DiscountingContext
     * either the same context if we have not applied the offer
     * or a new context which reflects any new discounts applied
     * and required items used up
     * @param context
     * @return
     */
    DiscountingContext apply(DiscountingContext context);

    /**
     * Is this offer valid for the given date
     * @param date
     * @return true if it is valid false otherwise
     */
    boolean isValidForDate(LocalDate date);

    /**
     * Is this offer potentially valid for the given collection of items
     *
     * This is an opportunity to count ourselves out of the running
     * if the basket is missing a key component of the offer
     *
     * @param items
     * @return true if the offer is valid for the given set of items
     */
    boolean isValidForBasket(Collection<Item> items);

}
