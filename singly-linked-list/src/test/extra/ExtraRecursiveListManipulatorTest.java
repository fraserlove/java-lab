package test.extra;

import impl.RecursiveListManipulator;
import interfaces.IListManipulator;

/**
 * Concrete JUnit test class (subclass of ListManipulatorTest) for testing the RecursiveListManipulator implementation.
 *
 */
public class ExtraRecursiveListManipulatorTest extends ExtraListManipulatorTest {

    @Override
    public IListManipulator makeListManipulator() {
        return new RecursiveListManipulator();
    }

}
