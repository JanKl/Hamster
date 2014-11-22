import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class FixedSizeStack {

    int top, numElements;
    int[] baseArray;
    int[] koernerArray;
    MeinHamster hamster;

    public FixedSizeStack(int maxSize) {
        top = 0;
        numElements = 0;
        baseArray = new int[maxSize];
        koernerArray = new int[maxSize];
        initKoernerArray();
    }

    public void push(int num,int reihe, int spalte) {
        if (numElements < baseArray.length) {
            numElements++;
        }
            baseArray[top] = num;
            if (Territorium.getAnzahlKoerner(reihe,spalte) >0){
            	koernerArray[top] = 1;
            }
            else {
            	koernerArray[top] = 0;
            }
        top = (top + 1) % baseArray.length;

        
    }

    public int pop() {
        if (numElements == 0) {
            return -1;  
        }
        numElements--;
		koernerArray[top]=0;
        top = (top == 0) ? (baseArray.length - 1) : (top - 1);
        return baseArray[top];
    }
    
    boolean pruefeKoernerAufStrecke(){
    	boolean koernerAufStrecke= false;
    	for(int i=0; i< koernerArray.length; i++){
    		if (koernerArray[i] ==1 ||koernerArray[i] == -1||koernerArray[i]==2){
    			koernerAufStrecke=true;
    		}
    	}
    	return koernerAufStrecke;
    }
    void initKoernerArray(){
    	for(int i=0; i< koernerArray.length; i++){
    		koernerArray[i]=-1;
    	}	
    }
    boolean genuegendKoerner() {
    	boolean koernerDa = false;
    	int anzahl=0;
    	if (numElements >=6) {
    	for (int i=1;i<=5;i++){
    		if (top-i < 0) {
    			if (koernerArray[koernerArray.length-1-i] == 1){
    				anzahl++;
    			}
    		} else{
    		if (koernerArray[top-i]==1) {
    			anzahl++;
    			}
    		}  		
    	}
    	if (anzahl>=4) {
    		koernerDa = true;
    	}
    }
    return koernerDa;
    }
    
    void letztesKornGenommen(){
    	if (top == 0) {
			koernerArray[koernerArray.length -2] =2;	
    	}
    	else {
    		koernerArray[top-1]=2; 
    	}  	
    }
}