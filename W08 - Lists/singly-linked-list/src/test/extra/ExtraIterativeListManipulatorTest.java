package test.extra;

import impl.IterativeListManipulator;
import interfaces.IListManipulator;

/**
 * Concrete JUnit test class (subclass of ExtraListManipulatorTest) for testing the IterativeListManipulator implementation.
 *
 */
public class ExtraIterativeListManipulatorTest extends ExtraListManipulatorTest {

    @Override
    public IListManipulator makeListManipulator() {
        return new IterativeListManipulator();
    }

}
