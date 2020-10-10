package impl;

import interfaces.IFactory;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;


/**
 * This class implements a singleton factory.
 *
 */
public final class Factory implements IFactory {

    private static IFactory factoryInstance = null;

    private Factory() {

    }

    /**
     * Method which returns an instance of the singleton Factory class.
     * @return the instance of the Factory
     */
    public static IFactory getInstance() {
        if (factoryInstance == null) {
            factoryInstance = new Factory();
        }
        return factoryInstance;
    }

    @Override
    public IVendingMachineProduct makeVendingMachineProduct(String laneCode, String description) {
        // TODO Auto-generated method stub
        IVendingMachineProduct vendingMachineProduct = new VendingMachineProduct(laneCode, description);
        return vendingMachineProduct;
    }

    @Override
    public IProductRecord makeProductRecord(IVendingMachineProduct vendingMachineProduct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IVendingMachine makeVendingMachine() {
        // TODO Auto-generated method stub
        return null;
    }

}
