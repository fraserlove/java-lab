package interfaces;

/**
 * Interface for a product that is sold in a vending machine in a specific lane.
 *
 */
public interface IVendingMachineProduct {

    /**
     * This method returns the product's lane code, i.e. which lane the product is in, e.g. A1, A2, A3, B1, ... in the vending machine.
     * @return the lane code for this product
     */
    String getLaneCode();

    /**
     * This method returns the product description, such as Irn Bru, or Haggis Crisps.
     * @return the description of the product
     */
    String getDescription();

}
