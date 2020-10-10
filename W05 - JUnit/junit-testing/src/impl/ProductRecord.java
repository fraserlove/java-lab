package impl;

import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IProductRecord;

/**
 * This class represents a ProductRecord, recording information relating to a product sold in a vending machine.
 *
 */
public class ProductRecord implements IProductRecord {

    @Override
    public IVendingMachineProduct getProduct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getNumberOfSales() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNumberAvailable() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void addItem() {
        // TODO Auto-generated method stub

    }

    @Override
    public void buyItem() throws ProductUnavailableException {
        // TODO Auto-generated method stub

    }

}
