package com.cloudburst.bjssbasket.repo;

import com.cloudburst.bjssbasket.model.Offer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Data store for offers
 */
public interface OfferRepository {


    /**
     * Return all the offers that are valid for the given date
     * that can include constant offers that have no date window too
     * @param targetDate
     * @return
     */
    List<Offer> findValidOffersForDate(LocalDate targetDate);

}
