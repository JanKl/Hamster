/**
 * MeinHamster-Klasse von Kai Holzer und Jan Kleinekort
 * Erweitert die Hamster-Klasse.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class MeinHamster extends Hamster {
	Akku akku;
	FixedSizeStack memory;
	
    MeinHamster() {
    	super();
    	erstelleAkku();
    	erstelleMemory();
    }
    
	MeinHamster(Hamster hamster) {
		super(hamster);
    	erstelleAkku();
    	erstelleMemory();
	}
	
	MeinHamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner) {
		super(reihe, spalte, blickrichtung, anzahlKoerner);
    	erstelleAkku();
    	erstelleMemory();
	}
	
	MeinHamster(int reihe, int spalte, int blickrichtung, int anzahlKoerner, int farbe) {
		super(reihe, spalte, blickrichtung, anzahlKoerner, farbe);
    	erstelleAkku();
    	erstelleMemory();
	}
	
	void erstelleAkku() {
		akku = new Akku();
	}
		
	void erstelleMemory() {
		memory  = new FixedSizeStack(10);
	}

	/**
	 * Bewegt den Hamster ein Feld vor und nimmt ein Korn auf, falls eins vorhanden ist.
	 */
	void meinVor() {
		if (akku.hatLadung()) {
			// Ziehe zunächst ein Feld vor und reduziere den Ladezustand des Akkus
			super.vor();
			speichereWeg();
			akku.reduziereLadung();
			
			if (akku.gibLadung() == 550) {
				schrittZurueck();
				schrittZurueck();
				schrittZurueck();
				schrittZurueck();
				schrittZurueck();
			}
			
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
	
	void speichereWeg(){
        memory.push(super.getBlickrichtung());     
       
    }
    
	void schrittZurueck(){
        int richtungLetzterSchritt = memory.pop();
        int blickrichtung = super.getBlickrichtung();
        
        //gegenüberliegende Richtung ermitteln
        if (richtungLetzterSchritt >= 2){
            richtungLetzterSchritt= richtungLetzterSchritt-2;
        }
        else {
            richtungLetzterSchritt= richtungLetzterSchritt+2;
        }
        
        while (richtungLetzterSchritt != blickrichtung) {
            linksUm();
            blickrichtung = super.getBlickrichtung();
        }
        vor();
    }
}
