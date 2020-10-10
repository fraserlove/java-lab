package impl;

import interfaces.IVendingMachineProduct;

/**
 * This class represents products that can be stocked and sold in a vending machine in a specific lane.
 *
 */
public class VendingMachineProduct implements IVendingMachineProduct {

    String laneCode;
    String description;

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
