package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import common.AbstractFactoryClient;
import interfaces.IVendingMachineProduct;

public class IVendingMachineProductTests extends AbstractFactoryClient {

    private IVendingMachineProduct vendingMachineProduct;

    /**
     * This checks that the factory was able to call a sensible constructor to get a non-null instance of IVendingMachineProduct.
     */
    @Test
    public void vendingMachineProductNotNull() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertNotNull(vendingMachineProduct);
    }

    @Test
    public void CheckAllAttributes() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", "Haggis Crisps");
        assertEquals(vendingMachineProduct.getDescription(), "Haggis Crisps");
        assertEquals(vendingMachineProduct.getLaneCode(), "A1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"Haggis Crisps", "Chocolate", "Water", "Cola", "2 Biscuits", "04efew30", "@:£$)fj", "", "[]559fHWAHcjf7e9hf", "a", " "})
    public void CheckDescription(String productDescription) {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", productDescription);
        assertEquals(vendingMachineProduct.getDescription(), productDescription);
    }

    @Test
    public void DescriptionNull() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct("A1", null);
        assertNull(vendingMachineProduct.getDescription());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A1", "A2", "A49320", "A0", "0A", "A0B", "394fj04", "", "{45-x42", "A", " ", "___", "lane code"})
    public void CheckLaneCode(String laneCode) {
        vendingMachineProduct = getFactory().makeVendingMachineProduct(laneCode, "Haggis Crisps");
        assertEquals(vendingMachineProduct.getLaneCode(), laneCode);
    }

    @Test
    public void LaneCodeNull() {
        vendingMachineProduct = getFactory().makeVendingMachineProduct(null, "Haggis Crisps");
        assertNull(vendingMachineProduct.getLaneCode());
    }
}
