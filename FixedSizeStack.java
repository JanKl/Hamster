import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class FixedSizeStack {

    int top, numElements;
    int[] baseArray;

    public FixedSizeStack(int maxSize) {
        top = 0;
        numElements = 0;
        baseArray = new int[maxSize];
    }

    public void push(int num) {
        if (numElements < baseArray.length) {
            numElements++;
        }
            baseArray[top] = num;
            top = (top + 1) % baseArray.length;
        
    }

    public int pop() {
        if (numElements == 0) {
            return -1;  
        }
        numElements--;

        top = (top == 0) ? (baseArray.length - 1) : (top - 1);
        return baseArray[top];
    }
}