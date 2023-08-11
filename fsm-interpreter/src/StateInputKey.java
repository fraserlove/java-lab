/**
 *
 * A class used as a wrapper key object so that a dictionary with a pair of states and inputs can be used as a key.
 *
 */

/* The code below was modified from a post by Tomasz Nurkiewicz at https://stackoverflow.com/questions/14677993/how-to-create-a-hashmap-with-two-keys-key-pair-value (last accessed 2020-10-04) */
/* BEGIN Modified Code */
public class StateInputKey {

    private String state;
    private String input;

    public StateInputKey(String state, String input) {
        this.state = state;
        this.input = input;
    }

    // Within the containsKey() method from the HashMap class, Java uses equals to compare two keys and check if they are
    // equal. Therefore this method needs overridden to account for the fact this class has a pair of states and inputs
    // being used as keys.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateInputKey)) return false;
        StateInputKey key = (StateInputKey) o;
        return state.equals(key.state) && input.equals(key.input);
    }

    // The method to generate a hash code for each key in the HashMap has to be overridden to allow for mapping from
    // the new state and input keys. Note that the code below is an extension of the original hashCode() method and
    // so will should have a minimum amount of collisions comparable to the original hash function.
    @Override
    public int hashCode() {
        int hash = 31 * (int) state.charAt(0) + input.charAt(0);
        return hash;
    }
    /* END Modified Code */

    public String getState() {
        return state;
    }

    public String getInput() {
        return input;
    }

}