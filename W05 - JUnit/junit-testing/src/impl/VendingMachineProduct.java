package impl;

import interfaces.IVendingMachineProduct;

/**
 * This class represents products that can be stocked and sold in a vending machine in a specific lane.
 *
 */
public class VendingMachineProduct implements IVendingMachineProduct {

    private String laneCode;
    private String description;

    /**
     * Constructor method initialises the two attributes mentioned below.
     * @param laneCode a string used to identify which lane the product is in
     * @param description a string used to identify and describe the object
     */
    public VendingMachineProduct(String laneCode, String description) {
        this.laneCode = laneCode;
        this.description = description;
    }

    @Override
    public String getLaneCode() {
        return laneCode;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
