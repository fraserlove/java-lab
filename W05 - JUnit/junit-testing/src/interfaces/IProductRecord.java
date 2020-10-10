package interfaces;

import common.ProductUnavailableException;

/**
 * This is the interface for a ProductRecord, recording information relating to a product sold in a vending machine.
 *
 */
public interface IProductRecord {

    /**
     * Returns the product object associated with this record.
     * @return the product associated with this record
     */
    IVendingMachineProduct getProduct();


    /**
     * Returns the number of times this product has been bought.
     * @return the number of sales for the product
     */
    int getNumberOfSales();

    /**
     * Returns the number available in the machine for the given product.
     * @return the number available for this product in the machine
     */
    int getNumberAvailable();

    /**
     * Adds one item to the vending machine for the associated product line - if the product is "Haggis Crisps" then this method adds one bag of Haggis Crisps to the machine.
     */
    void addItem();


    /**
     * Processes the purchase of one item in this product line.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    void buyItem() throws ProductUnavailableException;


}
