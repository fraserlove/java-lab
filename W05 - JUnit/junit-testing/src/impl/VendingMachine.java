package impl;


import common.AbstractFactoryClient;
import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a simple vending machine which can stock and sell products.
 *
 */
public class VendingMachine extends AbstractFactoryClient implements IVendingMachine {

    HashMap<String, IProductRecord> Products = new HashMap<>();
    int noOfItems = 0;

    @Override
    public void registerProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeAlreadyInUseException {
        IProductRecord product = new ProductRecord(vendingMachineProduct);
        if (!Products.keySet().contains(vendingMachineProduct.getLaneCode())) {
            Products.put(vendingMachineProduct.getLaneCode(), product);
        }
        else {
            throw new LaneCodeAlreadyInUseException();
        }
    }

    @Override
    public void unregisterProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeNotRegisteredException {
        if (Products.keySet().contains(vendingMachineProduct.getLaneCode())) {
            Products.remove(vendingMachineProduct.getLaneCode());
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public void addItem(String laneCode) throws LaneCodeNotRegisteredException {
        if (Products.keySet().contains(laneCode)) {
            Products.get(laneCode).addItem();
            noOfItems++;
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public void buyItem(String laneCode) throws ProductUnavailableException, LaneCodeNotRegisteredException {
        if (Products.keySet().contains(laneCode)) {
            Products.get(laneCode).buyItem();
            noOfItems--;
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public int getNumberOfProducts() {
        return Products.size();
    }

    @Override
    public int getTotalNumberOfItems() {
        return noOfItems;
    }

    @Override
    public int getNumberOfItems(String laneCode) throws LaneCodeNotRegisteredException {
        return Products.get(laneCode).getNumberAvailable();
    }

    @Override
    public int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException {
        return Products.get(laneCode).getNumberOfSales();
    }

    @Override
    public IVendingMachineProduct getMostPopular() throws LaneCodeNotRegisteredException {
        // Most popular meaning highest sales, if multiple products have the same number of sales the method picks
        // the most popular product to be the first one it comes across.
        IProductRecord mostPopular = null;
        for (IProductRecord record : Products.values()) {
            if (record.getNumberOfSales() > mostPopular.getNumberOfSales()) {
                mostPopular = record;
            }
        }
        return mostPopular.getProduct();
    }

}
