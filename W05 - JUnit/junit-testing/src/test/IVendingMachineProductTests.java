package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import common.AbstractFactoryClient;

import interfaces.IVendingMachineProduct;

/**
 * This is a test class designed to comprehensively test the IVendingMachineProduct interface.
 *
 */
public class IVendingMachineProductTests extends AbstractFactoryClient {

    private IVendingMachineProduct product;

    /**
     * Tests that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachineProduct.
     */
    @Test
    public void vendingMachineProductNotNull() {
        product = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(product);
    }

    /**
     * Tests that the laneCode and description and are present in the instance of IVendingMachineProduct.
     */
    @Test
    public void checkDescriptionAndLaneCode() {
        product = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertEquals(product.getDescription(), "Haggis Crisps");
        assertEquals(product.getLaneCode(), "A1");
    }

    /**
     * Tests that different descriptions (including single character and empty strings) are accepted by IVendingMachineProduct.
     * @param productDescription
     */
    @ParameterizedTest
    @ValueSource(strings = {"Haggis Crisps", "Chocolate", "", "a", " "})
    public void checkDescription(String productDescription) {
        product = getFactory().makeVendingMachineProduct("A1", productDescription);
        assertEquals(product.getDescription(), productDescription);
    }

    /**
     * Tests that the IVendingMachineProduct description can be null.
     */
    @Test
    public void descriptionNull() {
        product = getFactory().makeVendingMachineProduct("A1", null);
        assertNull(product.getDescription());
    }

    /**
     * Tests that different laneCodes (including single character and empty strings) are accepted by IVendingMachineProduct.
     * @param laneCode
     */
    @ParameterizedTest
    @ValueSource(strings = {"A1", "A2", "A49320", "A0", "0A", "A0B", "394fj04", "", " "})
    public void checkLaneCode(String laneCode) {
        product = getFactory().makeVendingMachineProduct(laneCode, "Haggis Crisps");
        assertEquals(product.getLaneCode(), laneCode);
    }

    /**
     * Tests that the IVendingMachineProduct laneCode can be null.
     */
    @Test
    public void laneCodeNull() {
        product = getFactory().makeVendingMachineProduct(null, "Haggis Crisps");
        assertNull(product.getLaneCode());
    }
}
