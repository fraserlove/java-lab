package interfaces;

import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;

/**
 * Interface for a simple vending machine ADT.
 *
 */
public interface IVendingMachine {


    /**
     * Registers the specified product for sale in the vending machine.
     * @param vendingMachineProduct the product to register
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    void registerProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeAlreadyInUseException;


    /**
     * Unregisters the specified product from the vending machine.
     * @param vendingMachineProduct the product to remove
     * @throws LaneCodeNotRegisteredException if the product's lane code has not been registered for use in the vending machine for the given product
     */
    void unregisterProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeNotRegisteredException;


    /**
     * Adds one item of stock to a vending machine lane.
     * @param laneCode the lane code of the product, e.g. A1, A2, A3, B1, ... in the vending machine
     * @throws LaneCodeNotRegisteredException if the lane code has not been registered for use in the vending machine
     */
    void addItem(String laneCode) throws LaneCodeNotRegisteredException;


    /**
     * Buys an item in the given vending machine lane.
     *
     * @param laneCode the lane code of the item, e.g. A1, A2, A3, B1, ... in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    void buyItem(String laneCode) throws ProductUnavailableException, LaneCodeNotRegisteredException;


    /**
     * Gets the number of different products available in the machine.
     * If the machine is setup to sell 2 different products, "Haggis Crisps" and "Irn Bru", then the return should be 2
     * regardless of how many bags of crips and cans of Irn Bru are in the machine
     *
     * @return the number of different products available in the vending machine
     */
    int getNumberOfProducts();


    /**
     * Gets the total count of all stock items in the machine over all products (i.e. lanes) in the vending machine.
     * For example, returns 3 if the vending machine has 2 cans of Irn Bru and 1 bag of Haggis Crisps.
     *
     * @return the total stock count over all products in the machine
     */
    int getTotalNumberOfItems();


    /**
     * Gets the number of items that are available to buy in the vending machine for a particular product (i.e. lane).
     *
     * @param laneCode the lane code in the machine
     * @return the number of items that are available to buy for a particular lane in the machine
     * @throws LaneCodeNotRegisteredException if the lane code has not been registered for use
     */
    int getNumberOfItems(String laneCode) throws LaneCodeNotRegisteredException;


    /**
     * Gets the total number of times that a given product (associated with a lane) was bought.
     *
     * @param laneCode the lane code of the product
     * @return the total number of times that the product has been bought
     * @throws LaneCodeNotRegisteredException if the lane code has not been registered for use in the machine
     */
    int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException;


    /**
     * Gets the product that has been bought the greatest number of times. The behaviour is undefined if there is not a single most popular product.
     *
     * @return the product that has been bought the greatest number of times
     * @throws LaneCodeNotRegisteredException if no lane codes have been registered for use in the vending machine
     */
    IVendingMachineProduct getMostPopular() throws LaneCodeNotRegisteredException;

}
