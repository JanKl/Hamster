/**
 * MeinHamster-Klasse von Kai Holzer und Jan Kleinekort
 * Erweitert die Hamster-Klasse.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class MeinHamster extends Hamster {
	Akku akku;
	
    MeinHamster() {
    	super();
    	erstelleAkku();
    }
    
	MeinHamster(Hamster hamster) {
		super(hamster);
    	erstelleAkku();
	}
	
	MeinHamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner) {
		super(reihe, spalte, blickrichtung, anzahlKoerner);
    	erstelleAkku();
	}
	
	MeinHamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner, int farbe) {
		super(reihe, spalte, blickrichtung, anzahlKoerner, farbe);
    	erstelleAkku();
	}
	
	void erstelleAkku() {
		akku = new Akku();
	}

	/**
	 * Bewegt den Hamster ein Feld vor und nimmt ein Korn auf, falls eins vorhanden ist.
	 */
	void meinVor() {
		if (akku.hatLadung()) {
			// Ziehe zunächst ein Feld vor und reduziere den Ladezustand des Akkus
			super.vor();
			akku.reduziereLadung();
			
			// Sammle ein Korn auf, falls auf diesem Feld vorhanden
			if (super.kornDa())  {
				super.nimm();
			}
			
		} else {
			super.schreib("Akku leer :-(");
		}
	}
	
	/**
	 * Führt eine Linksdrehung des Hamsters durch und reduziert die Akkuladung entsprechend.
	 */
	void meinLinksUm() {
		if (akku.hatLadung()) {
			super.linksUm();
			akku.reduziereLadung();
		} else {
			super.schreib("Akku leer :-(");
		}
	}
	
	/**
	 * Führt eine Rechtsdrehung des Hamsters durch und reduziert die Akkuladung entsprechend.
	 */
	void meinRechtsUm() {
		if (akku.hatLadung()) {
			super.linksUm();
			super.linksUm();
			super.linksUm();
			akku.reduziereLadung();
		} else {
			super.schreib("Akku leer :-(");
		}
	}
	
	void meinDreheUm() {
		meinRechtsUm();
		meinRechtsUm();
	}
}
