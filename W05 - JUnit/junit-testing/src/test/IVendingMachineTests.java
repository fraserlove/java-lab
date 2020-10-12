package test;

import interfaces.IVendingMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import common.AbstractFactoryClient;
import common.LaneCodeAlreadyInUseException;
import common.LaneCodeNotRegisteredException;
import common.ProductUnavailableException;

import interfaces.IVendingMachineProduct;

/**
 * This is a test class designed to comprehensively test the IVendingMachine interface.
 *
 */
public class IVendingMachineTests extends AbstractFactoryClient {

    private IVendingMachineProduct product;
    private IVendingMachineProduct otherProduct;
    private IVendingMachine vendingMachine;

    /**
     * Called before each test method. Creates a instance of IProductRecord and passes it into the constructor when
     * creating a IVendingMachine object.
     */
    @BeforeEach
    public void createVendingMachineProduct() {
        product = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        otherProduct = getFactory().makeVendingMachineProduct("A2", "Salted Crisps");
        vendingMachine = getFactory().makeVendingMachine();
    }

    /**
     * Tests that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachine.
     */
    @Test
    public void vendingMachineNotNull() {
        assertNotNull(vendingMachine);
    }

    /**
     * Tests that a product has been successfully registered within the IVendingMachine in a specific lane.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkRegisterProduct() throws LaneCodeAlreadyInUseException {
        assertEquals(0, vendingMachine.getNumberOfProducts());
        vendingMachine.registerProduct(product);
        assertEquals(1, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that IVendingMachine throws a NullPointerException when trying to register a null product.
     */
    @Test
    public void checkRegisterNullProduct() {
        assertEquals(0, vendingMachine.getNumberOfProducts());
        assertThrows(NullPointerException.class, () -> {
            vendingMachine.registerProduct(null);
        });
        assertEquals(0, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that the IVendingMachine throws a LaneCodeAlreadyInUseException when trying to register a product that has
     * already been registered to that lane code in the machine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkRegisterProductLaneAlreadyInUse() throws LaneCodeAlreadyInUseException {
        assertEquals(0, vendingMachine.getNumberOfProducts());
        vendingMachine.registerProduct(product);
        assertEquals(1, vendingMachine.getNumberOfProducts());
        assertThrows(LaneCodeAlreadyInUseException.class, () -> {
            vendingMachine.registerProduct(product);
        });
        assertEquals(1, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that a product has been successfully unregistered from the IVendingMachine and is no longer available.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    @Test
    public void checkUnregisterProduct() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(product);
        assertEquals(1, vendingMachine.getNumberOfProducts());
        vendingMachine.unregisterProduct(product);
        assertEquals(0, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that the IVendingMachine throws a LaneCodeNotRegisteredException when trying to unregister a product that
     * does not exist.
     */
    @Test
    public void checkUnregisterProductLaneCodeNotRegistered() {
        assertEquals(0, vendingMachine.getNumberOfProducts());
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.unregisterProduct(product);
        });
        assertEquals(0, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that a IVendingMachine adds an item on the specified lane.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    @Test
    public void checkAddItem() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(product);
        String laneCode = product.getLaneCode();

        assertEquals(0, vendingMachine.getNumberOfItems(laneCode));
        vendingMachine.addItem(laneCode);
        assertEquals(1, vendingMachine.getNumberOfItems(laneCode));
    }

    /**
     * Tests that IVendingMachine adds two different products on separate lanes correctly.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    @Test
    public void checkAddItemsFromMultipleLanes() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        vendingMachine.addItem(lane);
        vendingMachine.addItem(otherLane);
        assertEquals(1, vendingMachine.getNumberOfItems(lane));
        assertEquals(1, vendingMachine.getNumberOfItems(otherLane));
    }

    /**
     * Tests that the IVendingMachine throws a LaneCodeNotRegisteredException when trying to add an item to a lane code that
     * does not exist.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkAddItemLaneCodeNotRegistered() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(product);
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.addItem("A random lane code");
        });
    }

    /**
     * Tests that a IVendingMachine buys an item from the specified lane.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkBuyItem() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        String lane = product.getLaneCode();

        vendingMachine.addItem(lane);
        assertEquals(1, vendingMachine.getNumberOfItems(lane));
        vendingMachine.buyItem(lane);
        assertEquals(0, vendingMachine.getNumberOfItems(lane));
    }

    /**
     * Tests that the IVendingMachine throws a LaneCodeNotRegisteredException when trying to buy an item from a lane code that
     * does not exist.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkBuyItemLaneCodeNotRegistered() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(product);
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.buyItem("A random lane code");
        });
    }

    /**
     * Tests that IVendingMachine buys two different products from separate lanes correctly.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkBuyItemsFromMultipleLanes() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        vendingMachine.addItem(lane);
        vendingMachine.addItem(otherLane);
        assertEquals(1, vendingMachine.getNumberOfItems(lane));
        assertEquals(1, vendingMachine.getNumberOfItems(otherLane));
        vendingMachine.buyItem(lane);
        vendingMachine.buyItem(otherLane);
        assertEquals(0, vendingMachine.getNumberOfItems(lane));
        assertEquals(0, vendingMachine.getNumberOfItems(otherLane));
    }

    /**
     * Tests that IVendingMachine throws a ProductUnavailableException when trying to buy an item from a lane in which
     * there are no products left to buy.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkBuyItemsProductUnavailable() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(product);
        String lane = product.getLaneCode();
        assertThrows(ProductUnavailableException.class, () -> {
            vendingMachine.buyItem(lane);
        });
    }

    /**
     * Tests that the correct value for the number of products registered is returned by the IVendingMachine class.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    @Test
    public void checkGetNumberOfProducts() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        assertEquals(0, vendingMachine.getNumberOfProducts());
        vendingMachine.registerProduct(product);
        assertEquals(1, vendingMachine.getNumberOfProducts());
        vendingMachine.registerProduct(otherProduct);
        assertEquals(2, vendingMachine.getNumberOfProducts());
        vendingMachine.unregisterProduct(product);
        assertEquals(1, vendingMachine.getNumberOfProducts());
        vendingMachine.unregisterProduct(otherProduct);
        assertEquals(0, vendingMachine.getNumberOfProducts());
    }

    /**
     * Tests that IVendingMachine calculates the correct value for the total number of items over all products in the machine.
     * The test checks if this value is correct when we add and buy multiple items from multiple lanes in the IVendingMachine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkGetTotalNumberOfItems() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        assertEquals(0, vendingMachine.getTotalNumberOfItems());
        vendingMachine.addItem(lane);
        assertEquals(1, vendingMachine.getTotalNumberOfItems());
        vendingMachine.addItem(otherLane);
        assertEquals(2, vendingMachine.getTotalNumberOfItems());
        vendingMachine.addItem(lane);
        vendingMachine.addItem(otherLane);
        assertEquals(4, vendingMachine.getTotalNumberOfItems());
        vendingMachine.buyItem(lane);
        assertEquals(3, vendingMachine.getTotalNumberOfItems());
        vendingMachine.buyItem(otherLane);
        assertEquals(2, vendingMachine.getTotalNumberOfItems());
        vendingMachine.buyItem(lane);
        vendingMachine.buyItem(otherLane);
        assertEquals(0, vendingMachine.getTotalNumberOfItems());
    }

    /**
     * Tests that IVendingMachine returns the correct value for the total number of items of one product. The test checks
     * if this value is correct when we add and buy multiple items from multiple lanes (which should not update the value
     * if not in the chosen lane) in the IVendingMachine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkGetNumberOfItems() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        assertEquals(0, vendingMachine.getNumberOfItems(lane));
        assertEquals(0, vendingMachine.getNumberOfItems(otherLane));
        vendingMachine.addItem(lane);
        assertEquals(1, vendingMachine.getNumberOfItems(lane));
        assertEquals(0, vendingMachine.getNumberOfItems(otherLane));
        vendingMachine.addItem(otherLane);
        assertEquals(1, vendingMachine.getNumberOfItems(lane));
        assertEquals(1, vendingMachine.getNumberOfItems(otherLane));
        vendingMachine.buyItem(lane);
        vendingMachine.buyItem(otherLane);
        assertEquals(0, vendingMachine.getNumberOfItems(lane));
        assertEquals(0, vendingMachine.getNumberOfItems(otherLane));
    }

    /**
     * Tests that IVendingMachine throws a LaneCodeNotRegisteredException when trying to check for the number of items
     * of a product in a lane code that does not exist.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkGetNumberOfItemsLaneCodeNotRegistered() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(product);
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getNumberOfItems("A random lane code");
        });
    }

    /**
     * Tests that IVendingMachine returns the correct value for the total number of sales of one product. The test checks
     * if this value is correct when buying multiple items from multiple lanes (which should not update the value if not
     * in the chosen lane) in the IVendingMachine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkNumberOfSales() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        vendingMachine.addItem(lane);
        vendingMachine.addItem(otherLane);
        assertEquals(0, vendingMachine.getNumberOfSales(lane));
        assertEquals(0, vendingMachine.getNumberOfSales(otherLane));
        vendingMachine.buyItem(lane);
        vendingMachine.buyItem(otherLane);
        assertEquals(1, vendingMachine.getNumberOfSales(lane));
        assertEquals(1, vendingMachine.getNumberOfSales(otherLane));
    }

    /**
     * Tests that IVendingMachine throws a LaneCodeNotRegisteredException when trying to check for the number of sales
     * of a product in a lane code that does not exist.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     */
    @Test
    public void checkGetNumberOfSalesLaneCodeNotRegistered() throws LaneCodeAlreadyInUseException {
        vendingMachine.registerProduct(product);
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getNumberOfSales("A random lane code");
        });
    }

    /**
     * Tests that the most popular product is calculated to be the product bought the most number of times.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkGetMostPopular() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        for (int i = 0; i < 5; i++) {
            vendingMachine.addItem(lane);
            vendingMachine.buyItem(lane);
        }

        for (int i = 0; i < 10; i++) {
            vendingMachine.addItem(otherLane);
            vendingMachine.buyItem(otherLane);
        }
        assertEquals(otherProduct, vendingMachine.getMostPopular());
    }

    /**
     * Tests that IVendingMachine throws a LaneCodeNotRegisteredException when trying to check for the most popular product
     * when no products have registered yet.
     */
    @Test
    public void checkGetMostPopularLaneCodeNotRegistered() {
        assertThrows(LaneCodeNotRegisteredException.class, () -> {
            vendingMachine.getMostPopular();
        });
    }

    /**
     * Tests that IVendingMachine can return the most popular product when products with no items are present in the machine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     */
    @Test
    public void checkGetMostPopularOnProductWithNoItems() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException {
        vendingMachine.registerProduct(product);
        assertEquals(product, vendingMachine.getMostPopular());
    }

    /**
     * Tests that IVendingMachine can return the most popular product when only one product is present but has items in the machine.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkGetMostPopularOnProductWithOneProduct() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        vendingMachine.registerProduct(product);
        String lane = product.getLaneCode();

        for (int i = 0; i < 5; i++) {
            vendingMachine.addItem(lane);
            vendingMachine.buyItem(lane);
        }

        assertEquals(product, vendingMachine.getMostPopular());
    }

    /**
     * Tests that IVendingMachine can return the most popular product when multiple products have the same number of sales.
     * This should return the first product it comes across as the behaviour is undefined.
     * @throws LaneCodeAlreadyInUseException if a product is already registered in the vending machine with matching lane code
     * @throws LaneCodeNotRegisteredException if the given lane code has not been registered for use in the vending machine
     * @throws ProductUnavailableException if the specified lane is empty
     */
    @Test
    public void checkGetMostPopularWithTiedProducts() throws LaneCodeAlreadyInUseException, LaneCodeNotRegisteredException, ProductUnavailableException {
        IVendingMachineProduct otherProduct = getFactory().makeVendingMachineProduct("A2", "Salted Crisps");
        vendingMachine.registerProduct(product);
        vendingMachine.registerProduct(otherProduct);
        String lane = product.getLaneCode();
        String otherLane = otherProduct.getLaneCode();

        for (int i = 0; i < 5; i++) {
            vendingMachine.addItem(lane);
            vendingMachine.buyItem(lane);
            vendingMachine.addItem(otherLane);
            vendingMachine.buyItem(otherLane);
        }

        assertEquals(product, vendingMachine.getMostPopular());
    }

}
