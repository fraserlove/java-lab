package impl;

import common.AbstractFactoryClient;
import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;
import java.util.HashMap;

/**
 * This class represents a simple vending machine which can stock and sell products.
 *
 */
public class VendingMachine extends AbstractFactoryClient implements IVendingMachine {

    // Most efficient and optimal Java collection for storing IProductRecords with O(1) contains
    private HashMap<String, IProductRecord> products = new HashMap<>();
    private int noOfItems = 0; // Allows for constant time getTotalNumberOfItems()

    @Override
    public void registerProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeAlreadyInUseException {
        IProductRecord product = new ProductRecord(vendingMachineProduct);
        if (!products.keySet().contains(vendingMachineProduct.getLaneCode())) {
            products.put(vendingMachineProduct.getLaneCode(), product);
        }
        else {
            throw new LaneCodeAlreadyInUseException();
        }
    }

    @Override
    public void unregisterProduct(IVendingMachineProduct vendingMachineProduct) throws LaneCodeNotRegisteredException {
        if (products.keySet().contains(vendingMachineProduct.getLaneCode())) {
            products.remove(vendingMachineProduct.getLaneCode());
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public void addItem(String laneCode) throws LaneCodeNotRegisteredException {
        if (products.keySet().contains(laneCode)) {
            products.get(laneCode).addItem();
            noOfItems++;
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public void buyItem(String laneCode) throws ProductUnavailableException, LaneCodeNotRegisteredException {
        if (products.keySet().contains(laneCode)) {
            products.get(laneCode).buyItem();
            noOfItems--;
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public int getNumberOfProducts() {
        return products.size();
    }

    @Override
    public int getTotalNumberOfItems() {
        return noOfItems;
    }

    @Override
    public int getNumberOfItems(String laneCode) throws LaneCodeNotRegisteredException {
        if (products.keySet().contains(laneCode)) {
            return products.get(laneCode).getNumberAvailable();
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public int getNumberOfSales(String laneCode) throws LaneCodeNotRegisteredException {
        if (products.keySet().contains(laneCode)) {
            return products.get(laneCode).getNumberOfSales();
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

    @Override
    public IVendingMachineProduct getMostPopular() throws LaneCodeNotRegisteredException {
        // If multiple products have the same number of sales the method picks the most popular product to be the first
        // one it comes across.
        if (products.keySet().size() > 0) {
            // Setting the initial most popular IProductRecord to the first one present in the hashmap.
            IProductRecord mostPopular = (IProductRecord) products.values().toArray()[0];
            for (IProductRecord record : products.values()) {
                if (record.getNumberOfSales() > mostPopular.getNumberOfSales()) {
                    mostPopular = record;
                }
            }
            return mostPopular.getProduct();
        }
        else {
            throw new LaneCodeNotRegisteredException();
        }
    }

}
