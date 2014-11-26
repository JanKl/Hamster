import java.util.ArrayList;
import java.util.Random;

/**
 * MeinHamster-Klasse von Kai Holzer und Jan Kleinekort Erweitert die
 * Hamster-Klasse.
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class MeinHamster extends Hamster {

    Akku akku;
    FixedSizeStack memory;
    int[][] scannedTerritorium = new int[5][3];

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
        memory = new FixedSizeStack(10);
    }

    /**
     * Bewegt den Hamster ein Feld vor und nimmt ein Korn auf, falls eins
     * vorhanden ist.
     */
    void meinVor() {
        if (akku.hatLadung()) {
            // Ziehe zunächst ein Feld vor und reduziere den Ladezustand des Akkus

            // Prüfe vor dem Zug, ob eine Mauer da ist, um eine Exception zu vermeiden
            if (istMauerDa(Richtung.Geradeaus)) {
                super.schreib("Mauer vorne, Hamster will keine platte Nase.");
            } else {                                         
                super.vor(); 
                akku.reduziereLadung();
			}
        } else {
            super.schreib("Akku leer :-(");
        }
    }
    /** 
     * Speichert den Weg und läuft dann nach vorne.
     */
    void  meinVorUndSpeichern() {
    	speichereWeg();
    	meinVor();
    }
    
    /**
     * Nimmt ein Korn auf, falls auf diesem Feld eines liegt
     */
    void nimmKornFallsDa() {
		// Sammle ein Korn auf, falls auf diesem Feld vorhanden und überprüfe ob
		// dies das letzte Korn war.
		if (super.kornDa()) {
			super.nimm();
			if(!super.kornDa()){
				memory.letztesKornGenommen();
			}
		}
    }

    /**
     * Führt eine Linksdrehung des Hamsters durch und reduziert die Akkuladung
     * entsprechend.
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
     * Führt eine Rechtsdrehung des Hamsters durch und reduziert die Akkuladung
     * entsprechend.
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

    /**
     * Dreht den Hamster um und reduziert die Akkuladung.
     */
    void meinDreheUm() {
        meinLinksUm();
        meinLinksUm();
    }

    /**
     * Speichert den Weg der gelaufen wurde
     */
    void speichereWeg() {
        memory.push(super.getBlickrichtung(), super.getReihe(), super.getSpalte());

    }

	/**
	 * Läuft eine angegebene Anzahl an Schritten zurück und speichert die dafür benötigten
	 * Schritte wieder.
	 * @param anzahl Die Anzahl an Schritten, die zurück gegangen werden soll.
     */
    // TODO: Vor Bewegungen prüfen, ob Akku noch Ladung hat
    void schrittZurueck(int anzahl) {
        int[][] gespeicherteBlickrichtungen = new int[6][3];
        int blickrichtung;
        int gewuenschteRichtung;
        for (int i = anzahl; i > 0; i--) {
            blickrichtung = super.getBlickrichtung();
			gewuenschteRichtung = entgegengesetzteRichtung(memory.pop());
	
            while (gewuenschteRichtung != blickrichtung) {
                meinLinksUm();
                blickrichtung = super.getBlickrichtung();
            }
            gespeicherteBlickrichtungen[i][0]= blickrichtung;
            gespeicherteBlickrichtungen[i][1]= super.getReihe();
            gespeicherteBlickrichtungen[i][2]= super.getSpalte();
            meinVor();
           	nimmKornFallsDa();
        }
        for (int i= anzahl; i>0;i--){
           memory.push(gespeicherteBlickrichtungen[i][0],gespeicherteBlickrichtungen[i][1],gespeicherteBlickrichtungen[i][2]);
      	}
    }

    /**
     * Die entgegengesetze Richtung des gespeicherten Wertes wird ermittelt um
     * in diese Richtung zurück zu gehen.
     *
     * @param richtungLetzterSchritt gibt die gespeicherte Blickrichtung an.
     * @return
     */
    int entgegengesetzteRichtung(int richtungLetzterSchritt) {
        int gewuenschteRichtung =-1;
        if (richtungLetzterSchritt >= 2) {
            gewuenschteRichtung = richtungLetzterSchritt - 2;
        } else {
            gewuenschteRichtung = richtungLetzterSchritt + 2;
        }
        return gewuenschteRichtung;

    }
    /**
     *	Schaut ob auf 4 der letzten 5 Feldern Körner liegen und geht
     *  zurück um diese zu nehemen.
     */
    void ueberpruefeWeg(){
    	if (memory.genuegendKoerner()) {
    		schrittZurueck(5);
    	}
    }
	
	

    /**
     * Scannt die Umgebung vom aktuellen Standpunkt aus, überprüft ob eine Mauer
     * vorhanden ist und legt die ermittelten Werte in einem zweidimensionalen
     * Array ab. -1 --> Keine Mauer auf dieser Koordinate 0 --> Keine Aussage
     * über die Koordinate möglich 1 --> Auf dieser Koordinate befindet sich
     * eine Mauer
     *
     * kleine Hilfestellung zu Koordinaten
     * 0,0 1,0 2,0 3,0 4,0
     * 0,1 1,1 2,1 3,1 4,1
     * 0,2 1,3 Ham 3,2 4,2
     * ändere ich aber bei Gelegenheit noch zu dem "System" von getPosition()
     *     
*/
    void scanTerritorium() {

        // Den Array mit 0 initialisieren bzw. resetten
        initialisiereScannedTerritorium();
        int[][] umgebung = getPositions();

        // 0 0 0 0 0
        // 0 0 0 0 0
        // 0 X H 0 0 
        if (Territorium.mauerDa(umgebung[12][0], umgebung[12][1])) {
            scannedTerritorium[1][2] = 1;
        } else {
            scannedTerritorium[1][2] = -1;
        }
        // 0 0 0 0 0
        // 0 X 0 0 0
        // 0 0 H 0 0 
        if (Territorium.mauerDa(umgebung[7][0], umgebung[7][1])) {
            scannedTerritorium[1][1] = 1;
        } else {
            scannedTerritorium[1][1] = -1;
        }

        // 0 0 0 0 0
        // 0 0 X 0 0
        // 0 0 H 0 0 
        if (Territorium.mauerDa(umgebung[8][0], umgebung[8][1])) {
            scannedTerritorium[2][1] = 1;
        } else {
            scannedTerritorium[2][1] = -1;
        }
        // 0 0 0 0 0
        // 0 0 0 X 0
        // 0 0 H 0 0 
        if (Territorium.mauerDa(umgebung[9][0], umgebung[9][1])) {
            scannedTerritorium[3][1] = 1;
        } else {
            scannedTerritorium[3][1] = -1;
        }

        // 0 0 0 0 0
        // 0 0 0 0 0
        // 0 0 H X 0 
        if (Territorium.mauerDa(umgebung[14][0], umgebung[14][1])) {
            scannedTerritorium[3][2] = 1;
        } else {
            scannedTerritorium[3][2] = -1;
        }
        // 0 0 0 0 0
        // 0 0 0 0 0
        // X 0 H 0 0 
        if (scannedTerritorium[1][2] == -1) {
            if (Territorium.mauerDa(umgebung[11][0], umgebung[11][1])) {
                scannedTerritorium[0][2] = 1;
            } else {
                scannedTerritorium[0][2] = -1;
            }
        } else {
            scannedTerritorium[0][2] = 0;
        }

        // 0 0 0 0 0
        // X 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[1][2] == -1 || scannedTerritorium[1][1] == -1) {
            if (Territorium.mauerDa(umgebung[6][0], umgebung[6][1])) {
                scannedTerritorium[0][1] = 1;
            } else {
                scannedTerritorium[0][1] = -1;
            }
        } else {
            scannedTerritorium[0][1] = 0;
        }

        // X 0 0 0 0
        // 0 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[1][1] == -1) {
            if (Territorium.mauerDa(umgebung[1][0], umgebung[1][1])) {
                scannedTerritorium[0][0] = 1;
            } else {
                scannedTerritorium[0][0] = -1;
            }
        } else {
            scannedTerritorium[0][0] = 0;
        }

        // 0 X 0 0 0
        // 0 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[1][1] == -1 || scannedTerritorium[2][1] == -1) {
            if (Territorium.mauerDa(umgebung[2][0], umgebung[2][1])) {
                scannedTerritorium[1][0] = 1;
            } else {
                scannedTerritorium[1][0] = -1;
            }
        } else {
            scannedTerritorium[1][0] = 0;
        }
        // 0 0 X 0 0
        // 0 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[2][1] == -1) {
            if (Territorium.mauerDa(umgebung[3][0], umgebung[3][1])) {
                scannedTerritorium[2][0] = 1;
            } else {
                scannedTerritorium[2][0] = -1;
            }
        } else {
            scannedTerritorium[2][0] = 0;
        }

        // 0 0 0 X 0
        // 0 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[2][1] == -1 || scannedTerritorium[3][1] == -1) {
            if (Territorium.mauerDa(umgebung[4][0], umgebung[4][1])) {
                scannedTerritorium[3][0] = 1;
            } else {
                scannedTerritorium[3][0] = -1;
            }
        } else {
            scannedTerritorium[3][0] = 0;
        }

        // 0 0 0 0 X
        // 0 0 0 0 0
        // 0 0 H 0 0 
        if (scannedTerritorium[3][1] == -1) {
            if (Territorium.mauerDa(umgebung[5][0], umgebung[5][1])) {
                scannedTerritorium[4][0] = 1;
            } else {
                scannedTerritorium[4][0] = -1;
            }
        } else {
            scannedTerritorium[4][0] = 0;
        }

        // 0 0 0 0 0
        // 0 0 0 0 X
        // 0 0 H 0 0 
        if (scannedTerritorium[3][1] == -1 || scannedTerritorium[3][2] == -1) {
            if (Territorium.mauerDa(umgebung[10][0], umgebung[10][1])) {
                scannedTerritorium[4][1] = 1;
            } else {
                scannedTerritorium[4][1] = -1;
            }
        } else {
            scannedTerritorium[4][1] = 0;
        }

        // 0 0 0 0 0
        // 0 0 0 0 0
        // 0 0 H 0 X 
        if (scannedTerritorium[3][2] == -1) {
            if (Territorium.mauerDa(umgebung[15][0], umgebung[15][1])) {
                scannedTerritorium[4][2] = 1;
            } else {
                scannedTerritorium[4][2] = -1;
            }
        } else {
            scannedTerritorium[4][2] = 0;
        }

    }

    /**
     * Belegt den Array für das gescannte Gebiet komplett mit 0.
     *     
*/
    void initialisiereScannedTerritorium() {
        for (int i = 0; i < scannedTerritorium.length; i++) {
            for (int j = 0; j < scannedTerritorium[0].length; j++) {
                scannedTerritorium[i][j] = 0;
            }
        }

    }
	/**
	 * In dieser Methode werden die Koordinaten für die umliegenden
	 * Felder anhand der aktuellen Hamster-Position und Blickrichtung bestimmt.
	 * Diese werden dann fortlaufend in einen Array gespeichert.
	 * Dieser Array enthält dann in [...][1] die entsprechende Spalte
	 * und in [...][0] die entsprechende Reihe. 
	 * Die aufsteigenden Elemente sind im Blickfeld folgendermaßen angeordnet:
	 * 1  2  3  4  5
     * 6  7  8  9  10
     * 11 12 13 14 15
     * (wobei 13 die aktuelle Position des Hamsters ist)
     * @return umgebung gibt den 2-dimensionalen Array zurück, der die relativen
     * Koordinaten des Blickfeldes beinhaltet.
	 */
    int[][] getPositions() {
        int blickrichtung = super.getBlickrichtung();
        int[][] umgebung = new int[16][2];
        int hamsterPosSpalte = super.getSpalte();
        int hamsterPosReihe = super.getReihe();

        int speicherPos = 0;

        switch (blickrichtung) {
            //Nord
            case 0:
                speicherPos = 0;
                for (int y = -2; y <= 0; y++) {
                    for (int x = -2; x <= 2; x++) {
                        speicherPos++;
                        umgebung[speicherPos][1] = hamsterPosSpalte + x;
                        umgebung[speicherPos][0] = hamsterPosReihe + y;
                    }
                }
                break;
            //Ost
            case 1:
                speicherPos = 0;
                for (int x = +2; x >= 0; x--) {
                    for (int y = -2; y <= 2; y++) {
                        speicherPos++;
                        umgebung[speicherPos][1] = hamsterPosSpalte + x;
                        umgebung[speicherPos][0] = hamsterPosReihe + y;
                    }
                }
                break;
            //Süd
            case 2:
                speicherPos = 0;
                for (int y = +2; y >= 0; y--) {
                    for (int x = +2; x >= -2; x--) {
                        speicherPos++;
                        umgebung[speicherPos][1] = hamsterPosSpalte + x;
                        umgebung[speicherPos][0] = hamsterPosReihe + y;

                    }
                }
                break;
            //West
            case 3:
                speicherPos = 0;
                for (int x = -2; x <= 0; x++) {
                    for (int y = +2; y >= -2; y--) {
                        speicherPos++;
                        umgebung[speicherPos][1] = hamsterPosSpalte + x;
                        umgebung[speicherPos][0] = hamsterPosReihe + y;
                    }
                }
                break;
        }
        return umgebung;
    }

    void umgebungAnalysieren() {
        scanTerritorium();
        pruefeUmgebung();
    }
    /*
     * Prüft die Umgebung auf spezielle Anordnungen der Mauern im Sichtfeld
     * und reagiert auf diese entsprechend.
     *
     */

    void pruefeUmgebung() {
    	if (scannedTerritorium[1][2] == 1 && scannedTerritorium[2][1] == 1) {
            if (scannedTerritorium[3][2] == -1) {
                meinRechtsUm();
            } else {
                schrittZurueck(1);
            }
        }
     
    
        /*
         * Wenn auf den letzten 10 besuchten Feldern keine Körner mehr liegen,
         * soll der Hamster zufällig eine andere Richtung einschlagen. Wir er-
         * hoffen uns davon, dass über eine Zufallskomponente alle Felder ein-
         * mal besucht werden. Zumindest aber soll so verhindert werden, dass
         * immer der selbe leere Bereich abgesucht wird, wenn es noch Bereiche
         * mit Körnern gibt.
         * !memory.pruefeKoernerAufStrecke ergibt true, wenn die letzten 10 Felder leer
         * waren.
         */
        if (!memory.pruefeKoernerAufStrecke()) {
			// Finde zufällige Richtung und drehe in diese.
			dreheHamsterInRichtung(findeZufaelligeRichtung());

            // Setze die Information über leere Kacheln auf den letzten 10 Feldern zurück.
			// Wenn man das nicht macht, hat er möglicherweise beim nächsten Feld schon wieder 10 leere...
            memory.initKoernerArray();
        }

    }
	
	/**
	 * Finde eine zufällige, aber mögliche Drehrichtung
	 */
	Richtung findeZufaelligeRichtung() {
		/*
		 * Jetzt soll aus den Richtungen links, geradeaus und rechts zufällig
		 * eine ausgewählt werden. Es kommen jedoch nur die Richtungen in die
		 * Auswahl, bei denen der Hamster anschließend nicht direkt vor einer
		 * Mauer stehen würde.
		 * Zurück ist zunächst keine Option, da ja genau dort zuletzt auch
		 * keine Körner mehr lagen. Die Wahrscheinlichkeit ist also bei links,
		 * geradeaus und rechts höher.
		 */
		 
		// Initialisiere Array infrage kommender Drehrichtungen.
		ArrayList<Richtung> drehrichtungen = new ArrayList();

		// Prüfe infrage kommende Werte.
		if (!istMauerDa(Richtung.Links)) {
			drehrichtungen.add(Richtung.Links);
		}

		if (!istMauerDa(Richtung.Geradeaus)) {
			drehrichtungen.add(Richtung.Geradeaus);
		}

		if (!istMauerDa(Richtung.Rechts)) {
			drehrichtungen.add(Richtung.Rechts);
		}

		// Fall 0: Es wurden gar keine möglichen Drehrichtungen gefunden.
		// Dann: Drehe um und gehe zurück.
		if (drehrichtungen.size() == 0) {
			// DEBUG von hier
			//super.schreib("Keine möglichen Bewegungen nach vorne gefunden.");
			// DEBUG bis hier
			
			return Richtung.Zurueck;
		}

		// Fall 1: Es wurden nur eine mögliche Drehrichtung gefunden.
		// Dann: Wähle diese Richtung.
		if (drehrichtungen.size() == 1) {
			// DEBUG von hier
			//super.schreib("Nur " + drehrichtungen.get(0).toString() + " möglich.");
			// DEBUG bis hier
			
			return drehrichtungen.get(0);
		}

		// Fall 2: Es wurden mehrere mögliche Drehrichtungen gefunden.
		// Dann: Wähle per Zufall eine dieser Richtungen aus.
		if (drehrichtungen.size() > 1) {
			Random randomGenerator = new Random();
			int index = randomGenerator.nextInt(drehrichtungen.size());

			// DEBUG von hier
			ArrayList<String> zuege = new ArrayList();

			for (int i = 0; i < drehrichtungen.size(); i++) {
				zuege.add(drehrichtungen.get(i).toString());
			}

			String listString = "";

			for (String s : zuege) {
				listString += s + ", ";
			}

			//super.schreib("RAND: Möglich wären " + listString + "wähle " + drehrichtungen.get(index).toString() + ".");
			// DEBUG bis hier

			return drehrichtungen.get(index);
		}
		
		// Diese Ausgabe kann nie aufgerufen werden, Java fordert sie aber
		return Richtung.Geradeaus;
	}

    /**
     * Dreht den Hamster in eine durch drehrichtung gegebene Richtung
     *
     * @param drehrichtung Richtung, in die der Hamster jetzt schauen soll.
     */
    void dreheHamsterInRichtung(Richtung drehrichtung) {
        if (drehrichtung == Richtung.Links) {
            meinLinksUm();
            return;
        }

		// Bei Geradeaus müssen wir nichts tun
        if (drehrichtung == Richtung.Rechts) {
            meinRechtsUm();
            return;
        }

        if (drehrichtung == Richtung.Zurueck) {
            meinDreheUm();
            return;
        }
    }

    /**
     * Prüft, ob der Hamster ausgehend von der aktuellen Blickrichtung bei einer
     * Drehung in Richtung richtung vor einer Mauer stehen würde. Falls das
     * nicht entscheidbar ist (z.B. für zurück) wird false, also keine Mauer
     * zurück- gegeben.
     *
     * @param richtung Richtung für die geschaut werden soll. Muss Element des
     * ENUM Richtung sein.
     */
    boolean istMauerDa(Richtung richtung) {
        // Überprüfe zunächst die Umgebung, damit die Werte aktuell sind.
        scanTerritorium();

        // Prüfe für die gegebene Richtung, ob eine Mauer da ist.
        if (richtung == Richtung.Links) {
            return scannedTerritorium[1][2] == 1;
        }

        if (richtung == Richtung.Geradeaus) {
            return scannedTerritorium[2][1] == 1;
        }

        if (richtung == Richtung.Rechts) {
            return scannedTerritorium[3][2] == 1;
        }

        return false;
    }

	/**
	 * Zeigt ein Dialogfenster mit der Effizienz des Hamsters
	 */
	void zeigeEffizienz() {
		int gefundeneKoerner = super.getAnzahlKoerner();
		int zurueckgelasseneKoerner = Territorium.getAnzahlKoerner();
		int gesamteKoerner = gefundeneKoerner+zurueckgelasseneKoerner;
		int effizienz = Math.round(((float) gefundeneKoerner / (float) gesamteKoerner) * (float) 100);
		int restladung = akku.gibLadung();
		super.schreib("Von " + gesamteKoerner + " Körnern hat der Hamster " + gefundeneKoerner + " gefunden.\nZurückgelassen wurden " + zurueckgelasseneKoerner + " Körner. Das entspricht einer Effizienz von " + effizienz + "%.\nDie Restakkuladung sind " + restladung + " Einheiten.");
	}

	/**
	 * Reinigt das Feld mit dem Hamster
	 */
    void reinige() {
        while (akku.hatLadung()) {
        	// Prüfe, ob auf der aktuellen Kachel ein Korn liegt und nimm dieses ggf. auf
        	nimmKornFallsDa();
        	
        	// Prüfe nun, ob damit alle Körner in dem Territorium aufgesammelt sind.
        	if (Territorium.getAnzahlKoerner() == 0) {
        		break;
        	}
        
            ueberpruefeWeg();        	
            umgebungAnalysieren();

            //Wenn geradeaus eine Mauer ist, dann müssen wir einen anderen Weg finden.
            if (istMauerDa(Richtung.Geradeaus)) {
            	dreheHamsterInRichtung(findeZufaelligeRichtung());
            }
            
            // Durch eine Drehung ist jetzt möglicherweise der Akku leer geworden, deswegen neu prüfen
            if (akku.hatLadung()) {
	            // Es ist jetzt sicher nach vorne zu ziehen
    	        meinVorUndSpeichern();
    	    }
        }
        
        // Wenn wir hier angelangt sind, sind wir aus der while-Schleife geflogen.
        // Das passiert, wenn der Akku leer ist, oder alle Körner gefunden wurden.
		zeigeEffizienz();
    }
}
