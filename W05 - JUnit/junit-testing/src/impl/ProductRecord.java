package impl;

import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IProductRecord;

/**
 * This class represents a ProductRecord, recording information relating to a product sold in a vending machine.
 *
 */
public class ProductRecord implements IProductRecord {

    private IVendingMachineProduct vendingMachineProduct;
    private int noSales;
    private int noAvailable;

    /**
     * Constructor method initialises the vendingMachineProduct to the IVendingMachineObjet passed in.
     * @param vendingMachineProduct specifies the product related to this IProductRecord
     */
    public ProductRecord(IVendingMachineProduct vendingMachineProduct) {
        this.vendingMachineProduct = vendingMachineProduct;
        noSales = 0;
        noAvailable = 0;
    }

    @Override
    public IVendingMachineProduct getProduct() {
        return vendingMachineProduct;
    }

    @Override
    public int getNumberOfSales() {
        return noSales;
    }

    @Override
    public int getNumberAvailable() {
        return noAvailable;
    }

    @Override
    public void addItem() {
        noAvailable++;
    }

    @Override
    public void buyItem() throws ProductUnavailableException {
        if (noAvailable > 0) {
            noSales++;
            noAvailable--;
        }
        else {
            throw new ProductUnavailableException();
        }
    }

}
