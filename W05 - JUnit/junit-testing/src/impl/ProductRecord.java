package impl;

import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IProductRecord;

/**
 * This class represents a ProductRecord, recording information relating to a product sold in a vending machine.
 *
 */
public class ProductRecord implements IProductRecord {

    IVendingMachineProduct vendingMachineProduct;
    int noSales;
    int noAvailable;

    public ProductRecord(IVendingMachineProduct vendingMachineProduct) {
        this.vendingMachineProduct = vendingMachineProduct;
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
