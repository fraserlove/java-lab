package test;

import common.ProductUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import common.AbstractFactoryClient;
import interfaces.IProductRecord;
import interfaces.IVendingMachineProduct;

public class IProductRecordTests extends AbstractFactoryClient {

    private IVendingMachineProduct vendingMachineProduct;
    private IProductRecord productRecord;

    @BeforeEach
    public void createVendingMachineProduct() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
        productRecord = getFactory().makeProductRecord(vendingMachineProduct);
    }

    @Test
    public void productRecordNotNull() {
        assertNotNull(productRecord);
    }

    @Test
    public void checkVendingMachineProduct() {
        assertNotNull(productRecord.getProduct());
        assertEquals(productRecord.getProduct().getLaneCode(), "A1");
        assertEquals(productRecord.getProduct().getDescription(), "Haggis Crisps");
    }

    @Test
    public void checkBuyItem() throws ProductUnavailableException {
        productRecord.addItem();
        assertEquals(productRecord.getNumberAvailable(), 1);
        productRecord.buyItem();
        assertEquals(productRecord.getNumberAvailable(), 0);
    }

    @Test
    public void checkAddItem() throws ProductUnavailableException {
        assertEquals(productRecord.getNumberAvailable(), 0);
        productRecord.addItem();
        assertEquals(productRecord.getNumberAvailable(), 1);
    }

    @Test
    public void checkBuyItemException() throws ProductUnavailableException {
        assertThrows(ProductUnavailableException.class, () -> {
            productRecord.buyItem();
        });
        productRecord.addItem();
        assertEquals(productRecord.getNumberAvailable(), 1);
        productRecord.buyItem();
        assertEquals(productRecord.getNumberAvailable(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            productRecord.buyItem();
        });
        assertEquals(productRecord.getNumberAvailable(), 0);
    }

    @Test
    public void checkNoAvailable() throws ProductUnavailableException {
        assertEquals(productRecord.getNumberAvailable(), 0);
        productRecord.addItem();
        assertEquals(productRecord.getNumberAvailable(), 1);
        productRecord.addItem();
        productRecord.addItem();
        assertEquals(productRecord.getNumberAvailable(), 3);
        productRecord.buyItem();
        assertEquals(productRecord.getNumberAvailable(), 2);
        productRecord.buyItem();
        productRecord.buyItem();
        assertEquals(productRecord.getNumberAvailable(), 0);
    }

    @Test
    public void checkNoSales() throws ProductUnavailableException {
        assertEquals(productRecord.getNumberOfSales(), 0);
        productRecord.addItem();
        productRecord.addItem();
        productRecord.addItem();
        assertEquals(productRecord.getNumberOfSales(), 0);
        productRecord.buyItem();
        assertEquals(productRecord.getNumberOfSales(), 1);
        productRecord.buyItem();
        productRecord.buyItem();
        assertEquals(productRecord.getNumberOfSales(), 3);
    }

    @Test
    public void checkNoSalesWithProductUnavailableException() {
        assertEquals(productRecord.getNumberOfSales(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            productRecord.buyItem();
        });
        assertEquals(productRecord.getNumberOfSales(), 0);
    }

    @Test
    public void checkNoAvailableWithProductUnavailableException() {
        assertEquals(productRecord.getNumberAvailable(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            productRecord.buyItem();
        });
        assertEquals(productRecord.getNumberAvailable(), 0);
    }

    @Test
    public void largeCheckNoAvailableTest() {
        assertEquals(productRecord.getNumberAvailable(), 0);
        for (int i = 0; i < 1000; i++) {
            productRecord.addItem();
            assertEquals(productRecord.getNumberAvailable(), i + 1);
        }
        assertEquals(productRecord.getNumberAvailable(), 1000);
    }

    @Test
    public void largeCheckNoSalesTest() throws ProductUnavailableException{
        assertEquals(productRecord.getNumberOfSales(), 0);
        for (int i = 0; i < 1000; i++) {
            productRecord.addItem();
            productRecord.buyItem();
        }
        assertEquals(productRecord.getNumberOfSales(), 1000);
    }
}
