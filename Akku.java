/**
 * Akku berechnet aus der Territoriumsgr��e die Anfangsladung und bietet
 * Methoden um bei jedem Schritt den aktuellen Akkustand zu pr�fen und
 * zu reduzieren.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class Akku {
	int akkuladung;
	int akkuladungVoll;
	
	/**
	 * Initialisiert die Akkuladung mit der Territoriumsgr��e.
	 */
    Akku() {
    	akkuladung = 4 * Territorium.getAnzahlReihen() * Territorium.getAnzahlSpalten();
    	akkuladungVoll = akkuladung;
    }
    
    
    /**
     * Gibt die Info, ob noch Akkuladung vorhanden ist (f�r mindestens einen Schritt).
     * @return Ob noch Ladung vorhanden ist.
     */
    boolean hatLadung() {
    	return akkuladung > 0;
    }
    
    /**
     * Reduziert die Ladung um eine Einheit
     * @return Die aktuelle Ladung nachdem der Schritt gemacht wurde.
     */
    int reduziereLadung() {
    	return akkuladung -= 1;
    }
    
    
    /**
     * Gibt die aktuelle Ladung aus
     * @return Aktuelle Akkuladung
     */
    int gibLadung() {
    	return akkuladung;
    }
    
    /**
     * Gibt die maximale Ladung aus (Ladung zu Beginn)
     * @return gr��e des Akkus
     */
    int gibMaxLadung() {
    	return akkuladungVoll;
    }
        
        
}