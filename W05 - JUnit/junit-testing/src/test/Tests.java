package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import common.AbstractFactoryClient;
import interfaces.IVendingMachineProduct;
import interfaces.IVendingMachine;
import interfaces.IProductRecord;

/**
 * This is a JUnit test class for the Vending Machine ADT.
 */
public class Tests extends AbstractFactoryClient {

    /**
     * This checks that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachineProduct.
     */
   @Test
    public void vendingMachineProductNotNull() {
        IVendingMachineProduct vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
    }

    @Test
    public void productRecordNotNull() {
        IVendingMachineProduct vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        IProductRecord productRecord = getFactory().makeProductRecord(vendingMachineProduct);
        assertNotNull(productRecord);
    }
}
