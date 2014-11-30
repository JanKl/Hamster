import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;public class FixedSizeStack {

    int top, numElements;
    int[] baseArray;
    int[] koernerArray;
    int[] zwischenspeicher;
    int[] zwischenspeicher2;

    public FixedSizeStack(int maxSize) {
        numElements = 0;
        baseArray = new int[maxSize];
        koernerArray = new int[maxSize];
        zwischenspeicher = new int[maxSize + 2];
        zwischenspeicher2 = new int[maxSize + 2];
        initKoernerArray();
        initZwischenspeicher();
    }
    /*
     *Speichert die übergebenen Werte in einem Array ab. 
     *@param blickrichtung Gibt die zu speichernde Blickrichtung an.
     *@param reihe Gibt die Reihe der aktuellen Position an.
     *@param spalte Gibt die Spalte der aktuellen Position an.
     */

    public void push(int blickrichtung, int reihe, int spalte) {
        initZwischenspeicher();
        for (int i = 0; i < baseArray.length; i++) {
            zwischenspeicher[i + 2] = baseArray[i];
            zwischenspeicher2[i + 2] = koernerArray[i];
        }
        for (int i = 0; i < baseArray.length; i++) {
            baseArray[i] = zwischenspeicher[i + 1];
            koernerArray[i] = zwischenspeicher2[i + 1];
        }
        baseArray[0] = blickrichtung;
        if (numElements < baseArray.length) {
            numElements++;
        }
        if (Territorium.getAnzahlKoerner(reihe, spalte) > 0) {
            koernerArray[0] = 1;
        } else {
            koernerArray[0] = 0;
        }
    }
     /*
     *Gibt das oberste Element des Arrays aus und löscht dieses. 
     *Außerdem wird das oberste Element des Arrays in dem gespeichert ist ob 
     *Körner vorhanden sind gelöscht. 
     *@return zwischenspeicher[0] gibt den Wert des obersten Elements aus.
     */
    public int pop() {
        initZwischenspeicher();
        if (numElements == 0) {
            return -1;
        }

        for (int i = 0; i < baseArray.length; i++) {
            zwischenspeicher[i] = baseArray[i];
            zwischenspeicher2[i] = koernerArray[i];
        }
        for (int i = 0; i < baseArray.length; i++) {
            baseArray[i] = zwischenspeicher[i + 1];
            koernerArray[i] = zwischenspeicher2[i + 1];
        }
        numElements--;
        return zwischenspeicher[0];
    }
    
	 /*
     *Prüft ob auf der gespeicherten Strecke körner vorhanden sind.
     *@return koernerAufStrecke Gibt zurück ob Körner vorhanden oder nicht.
     */
    boolean pruefeKoernerAufStrecke() {
        boolean koernerAufStrecke = false;
        for (int i = 0; i < koernerArray.length; i++) {
            if (koernerArray[i] == 1 || koernerArray[i] == -1 || koernerArray[i] == 2) {
                koernerAufStrecke = true;
            }
        }
        return koernerAufStrecke;
    }
	 /*
     *Setzt den KoernerArray auf 0.
     */
    void initKoernerArray() {
        for (int i = 0; i < koernerArray.length; i++) {
            koernerArray[i] = -1;
        }
    }
	 /*
     *Setzt die beiden Zwischenspeicher auf 0.
     */
    void initZwischenspeicher() {
        for (int i = 0; i < zwischenspeicher.length; i++) {
            zwischenspeicher[i] = -1;
            zwischenspeicher2[i] = -1;
        }
    }
	 /*
     *Überprüft ob auf 4 der letzten 5 Feldern Körner vorhanden sind.
     *@return koernerDa Gibt an ob auf mindestens 4 der letzten 5 Feldern Körner sind.
     */
    boolean genuegendKoerner() {
        boolean koernerDa = false;
        int anzahl = 0;
        if (numElements >= 6) {
            for (int i = 1; i <= 5; i++) {
                if (koernerArray[i] == 1) {
                    anzahl++;
                }
            }
            if (anzahl >= 4) {
                koernerDa = true;
            }
        }
        return koernerDa;
    }
 	 /*
     *Setzt den KoernerArray auf 2, falls das letzte Korn genommen wurde.
     *Es wird dadurch vermieden, dass der Hamster meint es wären noch Körner
     *vorhanden und deshalb umkehrt.
     */
    void letztesKornGenommen() {
        koernerArray[0] = 2;
    }
}
