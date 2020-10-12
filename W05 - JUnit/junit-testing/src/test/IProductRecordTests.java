package test;

import common.ProductUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import common.AbstractFactoryClient;
import interfaces.IProductRecord;
import interfaces.IVendingMachineProduct;

/**
 * This is a test class designed to comprehensively test the IProductRecord interface.
 *
 */
public class IProductRecordTests extends AbstractFactoryClient {

    private IVendingMachineProduct product;
    private IProductRecord record;

    /**
     * Called before each test method. Creates a instance of IVendingMachineProduct and passes it into the constructor when
     * creating a IProductRecord object.
     */
    @BeforeEach
    public void createVendingMachineProduct() {
        product = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        record = getFactory().makeProductRecord(product);
    }

    /**
     * Tests that the factory was able to call a sensible constructor to get a non-null instance of IProductRecord.
     */
    @Test
    public void productRecordNotNull() {
        assertNotNull(record);
    }

    /**
     * Tests that the IProductRecord can still retrieve the correct IVendingMachineProduct using the getProduct() method
     * and that the IVendingMachineProduct holds the correct laneCode and description.
     */
    @Test
    public void checkGetProduct() {
        assertNotNull(record.getProduct());
        assertEquals(record.getProduct().getLaneCode(), "A1");
        assertEquals(record.getProduct().getDescription(), "Haggis Crisps");
    }

    /**
     * Tests that the buyItem() method works in buying an item that is available.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    @Test
    public void checkBuyItem() throws ProductUnavailableException {
        record.addItem();
        assertEquals(record.getNumberAvailable(), 1);
        record.buyItem();
        assertEquals(record.getNumberAvailable(), 0);
    }

    /**
     * Tests that the addItem() method works to add one new item to IProductRecord.
     */
    @Test
    public void checkAddItem() {
        assertEquals(record.getNumberAvailable(), 0);
        record.addItem();
        assertEquals(record.getNumberAvailable(), 1);
    }

    /**
     * Tests that the buyItem() method throws a ProductUnavailableException when buying an item that not available.
     * It checks this before any items have been added, and after an item has been bought and is no longer available.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    @Test
    public void checkBuyItemProductUnavailable() throws ProductUnavailableException {
        assertThrows(ProductUnavailableException.class, () -> {
            record.buyItem();
        });
        record.addItem();
        assertEquals(record.getNumberAvailable(), 1);
        record.buyItem();
        assertEquals(record.getNumberAvailable(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            record.buyItem();
        });
        assertEquals(record.getNumberAvailable(), 0);
    }

    /**
     * Tests that the getNumberAvailable() method correctly returns the number of products available
     * after multiple buyItem() and addItem() method calls.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    @Test
    public void checkNoAvailable() throws ProductUnavailableException {
        assertEquals(record.getNumberAvailable(), 0);
        record.addItem();
        assertEquals(record.getNumberAvailable(), 1);
        record.addItem();
        assertEquals(record.getNumberAvailable(), 2);
        record.buyItem();
        assertEquals(record.getNumberAvailable(), 1);
        record.buyItem();
        assertEquals(record.getNumberAvailable(), 0);
    }

    /**
     * Tests that the getNumberOfSales() method correctly returns the number of products sold
     * after multiple items have been bought.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    @Test
    public void checkNoSales() throws ProductUnavailableException {
        assertEquals(record.getNumberOfSales(), 0);
        record.addItem();
        record.addItem();
        record.addItem();
        assertEquals(record.getNumberOfSales(), 0);
        record.buyItem();
        assertEquals(record.getNumberOfSales(), 1);
        record.buyItem();
        assertEquals(record.getNumberOfSales(), 2);
    }

    /**
     * Tests that the number of sales does not change after the buyItem() method throws a ProductUnavailableException
     * if a product has tried to be sold that is not available.
     */
    @Test
    public void checkNoSalesWithProductUnavailable() {
        assertEquals(record.getNumberOfSales(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            record.buyItem();
        });
        assertEquals(record.getNumberOfSales(), 0);
    }

    /**
     * Tests that the amount of product available does not change after the buyItem() method throws a ProductUnavailableException
     * if a product has tried to be sold that is not available.
     */
    @Test
    public void checkNoAvailableWithProductUnavailable() {
        assertEquals(record.getNumberAvailable(), 0);
        assertThrows(ProductUnavailableException.class, () -> {
            record.buyItem();
        });
        assertEquals(record.getNumberAvailable(), 0);
    }

    /**
     * Tests that IProductRecord can add lots of the same item and can return the correct number of items available.
     */
    @Test
    public void largeCheckNoAvailableTest() {
        assertEquals(record.getNumberAvailable(), 0);
        for (int i = 0; i < 1000; i++) {
            record.addItem();
            assertEquals(record.getNumberAvailable(), i + 1);
        }
        assertEquals(record.getNumberAvailable(), 1000);
    }

    /**
     * Tests that IProductRecord can buy lots of the same item and can return the correct number of products sold.
     * @throws ProductUnavailableException when the product is not available in the machine
     */
    @Test
    public void largeCheckNoSalesTest() throws ProductUnavailableException {
        assertEquals(record.getNumberOfSales(), 0);
        for (int i = 0; i < 1000; i++) {
            record.addItem();
            record.buyItem();
        }
        assertEquals(record.getNumberOfSales(), 1000);
    }
}
