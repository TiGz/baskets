package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Encapsulate a Special Offer with description and rules
 *
 * A simple offer always has target item which is the type of
 * item the discount applies to.
 *
 * It can also optionally contain a required item that must be in the basket to
 * qualify for the discount
 *
 * Optionally it can contain a valid date range too
 *
 */
@Value.Immutable
@Serial.Structural
public abstract class SimpleOffer implements Offer {

    public abstract String getLabel();

    /**
     * A 10% discount means a discount multiplier of 0.1
     *
     * We should ask some questions of the client:
     *
     * Do they only want % discounts or do they also want
     * absolute discounts? For now we will KISS.
     *
     */
    public abstract BigDecimal getDiscountMultiplier();

    public abstract String getTargetItemCode();

    public abstract Optional<Integer> getTargetItemCount();

    public abstract Optional<String> getRequiredItemCode();

    public abstract Optional<Integer> getRequiredItemCount();

    public abstract Optional<LocalDate> getValidFrom();

    public abstract Optional<LocalDate> getValidUpto();


    /**
     * Apply this offer to the set of items in the given context and
     * if successful then add the appropriate discounts into the
     * new context
     * @param context
     * @return
     */
    @Override
    public DiscountingContext apply(DiscountingContext context) {
        while (isValidForBasket(context.getAvailableItems())){
            context = updateContext(context);
        }
        return context;
    }

    /**
     * Remove any required/target items and add in the discount
     * @param context
     * @return
     */
    protected DiscountingContext updateContext(DiscountingContext context){

        List<Item> items = new ArrayList(context.getAvailableItems());
        Collection<Item> targets = find(items,getTargetItemCode(),getTargetItemCount());
        items.removeAll(targets);

        if ( getRequiredItemCode().isPresent() ){
            Collection<Item> required = find(items,getRequiredItemCode().get(),getRequiredItemCount());
            items.removeAll(required);
        }

        BigDecimal discountAmount = targets.stream()
                .map (i -> calculateDiscount(i))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Discount discount = Discount.of(getLabel(),discountAmount);

        return ImmutableDiscountingContext.builder()
                .from(context)
                .addAppliedDiscounts(discount)
                .availableItems(items)
                .build();
    }

    BigDecimal calculateDiscount(Item item) {
        return new BigDecimal(-1).multiply(getDiscountMultiplier()).multiply(item.getPrice());
    }

    private Collection<Item> find(List<Item> items, String code, Optional<Integer> limit) {
        if ( limit.isPresent() ){
            return items.stream()
                    .filter(i -> i.getCode().equals(code))
                    .limit(limit.get())
                    .collect(Collectors.toList());
        }
        else{
            return items.stream()
                    .filter(i -> i.getCode().equals(code))
                    .collect(Collectors.toList());
        }
    }

    /**
     * false if the target item does not appear in the given items
     * or the required items do not appear in the given items
     * @param items
     * @return true if the offer has the potential to be applied
     */
    @Override
    public boolean isValidForBasket(Collection<Item> items) {
        boolean gotRequired = true;
        if ( getRequiredItemCode().isPresent() ){
            gotRequired = contains(items,getRequiredItemCode().get(),getRequiredItemCount().get());
        }
        return gotRequired && contains(items,getTargetItemCode(),1);
    }

    private static boolean contains (Collection<Item> items, String code, int count){
        return items.stream()
                .filter(i -> i.getCode().equals(code))
                .count() >= (long) count;
    }

    @Override
    public boolean isValidForDate(LocalDate date) {
        if ( getValidFrom().isPresent() ){
            if ( getValidFrom().get().isAfter(date) ) {
                return false;
            }
        }
        if ( getValidUpto().isPresent()) {
            if ( getValidUpto().get().isBefore(date) ){
                return false;
            }
        }
        return true;
    }
}
