package com.cloudburst.bjssbasket.model;

import org.immutables.serial.Serial;
import org.immutables.value.Value;

import java.math.BigDecimal;
import java.util.List;

/**
 * Encapsulate the total calculation for a basket of items plus discounts
 */
@Value.Immutable
@Serial.Structural
public abstract class BasketTotal {

    @Value.Parameter
    public abstract BigDecimal getSubTotal();

    @Value.Parameter
    public abstract List<Discount> getDiscounts();

    public BigDecimal calculateTotal(){
        BigDecimal totalDiscount = getDiscounts().stream()
                .map(d -> d.getAmount())
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        return getSubTotal().add(totalDiscount);
    }

    public String formatTotal(){
        StringBuilder sb = new StringBuilder();
        sb.append("Subtotal: ");
        sb.append(formatPrice(getSubTotal()));
        sb.append("\n");

        if ( getDiscounts().isEmpty() ){
            sb.append("(No offers available)\n");
        }
        else{
            getDiscounts().stream().sorted().forEach(
                d -> sb.append(d.getLabel() + ": " + formatPrice(d.getAmount())+"\n")
            );
        }

        sb.append ("Total price: ");
        sb.append ( formatPrice(calculateTotal() ) );

        return sb.toString();
    }

    /**
     * Format the given amount as pounds or pence
     * KISS - Assumung £ and p for now
     * @param amount
     * @return
     */
    static String formatPrice(BigDecimal amount) {
        if ( amount.abs().compareTo(BigDecimal.ONE) < 0 ){
            return round(amount).multiply(new BigDecimal(100d)).intValue() + "p";
        }
        else{
            return "£" + round(amount).toPlainString();
        }
    }

    private static BigDecimal round(BigDecimal amount){
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
