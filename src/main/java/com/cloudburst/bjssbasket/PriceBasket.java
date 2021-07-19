package com.cloudburst.bjssbasket;

import com.cloudburst.bjssbasket.model.Basket;
import com.cloudburst.bjssbasket.model.BasketTotal;
import com.cloudburst.bjssbasket.model.Item;
import com.cloudburst.bjssbasket.repo.HardCodedItemRepository;
import com.cloudburst.bjssbasket.repo.HardCodedOfferRepository;
import com.cloudburst.bjssbasket.services.BasketPricingService;
import com.cloudburst.bjssbasket.services.InventoryService;

import java.util.ArrayList;

/**
 * Main Entry point and simple assembled application
 */
public class PriceBasket {

    private InventoryService inventoryService = new InventoryService(new HardCodedItemRepository());
    private BasketPricingService pricingService = new BasketPricingService(new HardCodedOfferRepository());

    // emptry basket
    private Basket basket = Basket.of(new ArrayList<>());

    /**
     * Look up the item code in the inventory and add the associated
     * item to the basket
     * @param itemCode
     * @return the new basket containing the new item
     * @throws This will throw an ItemNotFoundException if the item code is invalid
     *
     * @TODO: perhaps we want to handle invalid items more gracefully than halting - consult with client
     */
    public Basket addItem (String itemCode){

        Item item = inventoryService.getStock(itemCode);
        basket = basket.addItems(item);

        return basket;
    }

    /**
     * Lookup relevant offers and find the best discounting strategy for the current basket
     * then return the total for the basket
     * @return the lowest basket total given the available offers
     */
    public BasketTotal calculateTotalPrice (){
        return pricingService.findBestDiscounts(basket);
    }

    public static void main (String[] itemCodes){

        try{
            PriceBasket priceBasket = new PriceBasket();

            for (String itemCode : itemCodes) {
                priceBasket.addItem(itemCode);
            }

            BasketTotal total = priceBasket.calculateTotalPrice();

            System.out.println(total.formatTotal());
        }
        catch (Exception ex){
            System.out.println("There was a problem with your order: " + ex.getMessage() );
        }
    }


}
