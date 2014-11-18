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
            super.vor();
            akku.reduziereLadung();

            // Sammle ein Korn auf, falls auf diesem Feld vorhanden
            if (super.kornDa()) {
                super.nimm();
            }

        } else {
            super.schreib("Akku leer :-(");
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
        meinRechtsUm();
        meinRechtsUm();
    }

    /**
     * Speichert den Weg der gelaufen wurde
     */
    void speichereWeg() {
        memory.push(super.getBlickrichtung(),super.getReihe(),super.getSpalte());

    }

    /**
     * Läuft einen Schritt der gespeichert wurde zurück
     */
    void schrittZurueck() {
        int richtungLetzterSchritt = memory.pop();
        int blickrichtung = super.getBlickrichtung();

        //gegenüberliegende Richtung ermitteln
        if (richtungLetzterSchritt >= 2) {
            richtungLetzterSchritt = richtungLetzterSchritt - 2;
        } else {
            richtungLetzterSchritt = richtungLetzterSchritt + 2;
        }

        while (richtungLetzterSchritt != blickrichtung) {
            meinLinksUm();
            blickrichtung = super.getBlickrichtung();
        }
        //meinVor() kann nicht verwendet werden, da sonst wieder der Weg gespeichert wird
        vor();
        akku.reduziereLadung();
    }

    /**
     * Scannt die Umgebung vom aktuellen Standpunkt aus, überprüft ob eine Mauer
     * vorhanden ist und legt die ermittelten Werte in einem zweidimensionalen
     * Array ab. -1 --> Keine Mauer auf dieser Koordinate 0 --> Keine Aussage
     * über die Koordinate möglich 1 --> Auf dieser Koordinate befindet sich
     * eine Mauer
     *     
*/
    void scanTerritorium() {

        // Den Array mit 0 initialisieren bzw. resetten
        initialiereScannedTerritorium();
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
    void initialiereScannedTerritorium() {
        for (int i = 0; i < scannedTerritorium.length; i++) {
            for (int j = 0; j < scannedTerritorium[0].length; j++) {
                scannedTerritorium[i][j] = 0;
            }
        }

    }

    int[][] getPositions() {
        int blickrichtung = super.getBlickrichtung();
        int[][] umgebung = new int[16][2];
        int hamsterPosSpalte = super.getSpalte();
        int hamsterPosReihe = super.getReihe();

        int speicherPos = 0;
        //speicherPos Anordnung
        // 1  2  3  4  5
        // 6  7  8  9  10
        // 11 12 13 14 15
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

    void pruefeUmgebung() {
        if (scannedTerritorium[1][2] == 1 && scannedTerritorium[2][1] == 1) {
            if (scannedTerritorium[3][2] == -1) {
                meinRechtsUm();
            } else {
                schrittZurueck();
            }
        }
        
        /*
         * Wenn auf den letzten 10 besuchten Feldern keine Körner mehr liegen,
         * soll der Hamster zufällig eine andere Richtung einschlagen. Wir er-
         * hoffen uns davon, dass über eine Zufallskomponente alle Felder ein-
         * mal besucht werden. Zumindest aber soll so verhindert werden, dass
         * immer der selbe leere Bereich abgesucht wird, wenn es noch Bereiche
         * mit Körnern gibt.
         * !memory.priefKoerner ergibt true, wenn die letzten 10 Felder leer
         * waren.
         */
        if (!memory.pruefeKoerner()){
        	/*
        	 * Jetzt soll aus den Richtungen links, vorn und rechts zufällig
        	 * eine ausgewählt werden. Es kommen jedoch nur die Richtungen in
        	 * die Auswahl, bei denen der Hamster anschließend nicht direkt vor
        	 * einer Mauer stehen würde.
        	 * Zurück ist zunächst keine Option, da ja genau dort zuletzt auch
        	 * keine Körner mehr lagen. Die Wahrscheinlichkeit ist also bei
        	 * links, vorn und rechts höher.
        	 */
        	
        	meinRechtsUm();
        	
        	// Was macht das hier?
        	memory.initKoernerArray();
        }
        
    }

    void reinige() {
        while (akku.hatLadung()) {
            umgebungAnalysieren();
            meinVor();
            speichereWeg();

        }
    }

}
