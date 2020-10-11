package test;

import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;
import interfaces.IVendingMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import common.AbstractFactoryClient;
import interfaces.IProductRecord;
import interfaces.IVendingMachineProduct;

public class IVendingMachineTests extends AbstractFactoryClient {

    private IVendingMachineProduct vendingMachineProduct;
    private IProductRecord productRecord;
    private IVendingMachine vendingMachine;

    @BeforeEach
    public void createVendingMachineProduct() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
        vendingMachine = getFactory().makeVendingMachine();
    }

    @Test
    public void vendingMachineNotNull() {
        assertNotNull(vendingMachine);
    }

    @Test
    public void checkRegisterProduct() throws LaneCodeAlreadyInUseException {
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
        vendingMachine.registerProduct(vendingMachineProduct);
        assertEquals(vendingMachine.getNumberOfProducts(), 1);
    }

    @Test
    public void checkRegisterNullProduct() throws LaneCodeAlreadyInUseException {
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
        assertThrows(NullPointerException.class, () -> {
            vendingMachine.registerProduct(null);
        });
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
    }

    @Test
    public void checkRegisterProductLaneAlreadyInUseException() throws LaneCodeAlreadyInUseException {
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
        vendingMachine.registerProduct(vendingMachineProduct);
        assertEquals(vendingMachine.getNumberOfProducts(), 1);
        assertThrows(LaneCodeAlreadyInUseException.class, () -> {
            vendingMachine.registerProduct(vendingMachineProduct);
        });
        assertEquals(vendingMachine.getNumberOfProducts(), 1);
    }

    @Test
    public void checkUnregisterProduct() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(vendingMachineProduct);
        assertEquals(vendingMachine.getNumberOfProducts(), 1);
        vendingMachine.unregisterProduct(vendingMachineProduct);
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
    }

    @Test
    public void checkUnregisterProductLaneCodeNotRegisteredException() {
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.unregisterProduct(vendingMachineProduct);
        });
        assertEquals(vendingMachine.getNumberOfProducts(), 0);
    }

    @Test
    public void checkAddItem() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(vendingMachineProduct);
        String laneCode = vendingMachineProduct.getLaneCode();
        assertEquals(vendingMachine.getNumberOfItems(laneCode), 0);
        vendingMachine.addItem(laneCode);
        assertEquals(vendingMachine.getNumberOfItems(laneCode), 1);
    }

    @Test
    public void checkAddItemsFromMultipleLanes() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        IVendingMachineProduct secondVendingMachineProduct = getFactory().makeVendingMachineProduct("A2", "Salted Crisps");
        vendingMachine.registerProduct(vendingMachineProduct);
        vendingMachine.registerProduct(secondVendingMachineProduct);
        String firstLaneCode = vendingMachineProduct.getLaneCode();
        String secondLaneCode = secondVendingMachineProduct.getLaneCode();
        vendingMachine.addItem(firstLaneCode);
        vendingMachine.addItem(secondLaneCode);
        assertEquals(vendingMachine.getNumberOfItems(firstLaneCode), 1);
        assertEquals(vendingMachine.getNumberOfItems(secondLaneCode), 1);
    }

    @Test
    public void checkAddItemLaneCodeNotRegisteredException() throws LaneCodeNotRegisteredException, LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(vendingMachineProduct);
        String laneCode = "A random lane code";
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.addItem(laneCode);
        });
    }



}
