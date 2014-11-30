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
    Richtung letzteRichtung = Richtung.Links;

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
            // Ziehe zun�chst ein Feld vor und reduziere den Ladezustand des Akkus

            // Pr�fe vor dem Zug, ob eine Mauer da ist, um eine Exception zu vermeiden
            if (istMauerDa(Richtung.Geradeaus)) {
                super.schreib("Mauer vorne, Hamster will keine platte Nase.");
            } else {
                super.vor();
                akku.reduziereLadung();
                // Pr�fe, ob auf der aktuellen Kachel ein Korn liegt und nimm dieses ggf. auf
                nimmKornFallsDa();
                scannedTerritorium = SichtfeldScanner.scanSichtfeld(super.getReihe(), super.getSpalte(), super.getBlickrichtung());
            }
        } else {
            super.schreib("Akku leer :-(");
        }
    }

    /**
     * Speichert den Weg und l�uft dann nach vorne.
     */
    void meinVorUndSpeichern() {
        speichereWeg();
        meinVor();
    }

    /**
     * Nimmt ein Korn auf, falls auf diesem Feld eines liegt
     */
    void nimmKornFallsDa() {
        // Sammle ein Korn auf, falls auf diesem Feld vorhanden und �berpr�fe ob
        // dies das letzte Korn war.
        if (super.kornDa()) {
            super.nimm();
            if (!super.kornDa()) {
                memory.letztesKornGenommen();
            }
        }
    }

    /**
     * F�hrt eine Linksdrehung des Hamsters durch und reduziert die Akkuladung
     * entsprechend.
     */
    void meinLinksUm() {
        if (akku.hatLadung()) {
            super.linksUm();
            akku.reduziereLadung();
            letzteRichtung = Richtung.Links;
            scannedTerritorium = SichtfeldScanner.scanSichtfeld(super.getReihe(), super.getSpalte(), super.getBlickrichtung());

        } else {
            super.schreib("Akku leer :-(");
        }
    }

    /**
     * F�hrt eine Rechtsdrehung des Hamsters durch und reduziert die Akkuladung
     * entsprechend.
     */
    void meinRechtsUm() {
        if (akku.hatLadung()) {
            super.linksUm();
            super.linksUm();
            super.linksUm();
            akku.reduziereLadung();
            letzteRichtung = Richtung.Rechts;
            scannedTerritorium = SichtfeldScanner.scanSichtfeld(super.getReihe(), super.getSpalte(), super.getBlickrichtung());
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
     * Speichert den Weg der gelaufen wurde.
     */
    void speichereWeg() {
        memory.push(super.getBlickrichtung(), super.getReihe(), super.getSpalte());

    }

    /**
     * L�uft eine angegebene Anzahl an Schritten zur�ck und speichert die daf�r
     * ben�tigten Schritte wieder in "memory" ab.
     *
     * @param anzahl Die Anzahl an Schritten, die zur�ck gegangen werden soll.
     */
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
            gespeicherteBlickrichtungen[i][0] = blickrichtung;
            gespeicherteBlickrichtungen[i][1] = super.getReihe();
            gespeicherteBlickrichtungen[i][2] = super.getSpalte();
            meinVor();
        }
        for (int i = anzahl; i > 0; i--) {
            memory.push(gespeicherteBlickrichtungen[i][0], gespeicherteBlickrichtungen[i][1], gespeicherteBlickrichtungen[i][2]);
        }
    }

    /**
     * Die entgegengesetze Richtung des gespeicherten Wertes wird ermittelt um
     * in diese Richtung zur�ck zu gehen.
     *
     * @param richtungLetzterSchritt gibt die gespeicherte Blickrichtung an.
     * @return
     */
    int entgegengesetzteRichtung(int richtungLetzterSchritt) {
        int gewuenschteRichtung = -1;
        if (richtungLetzterSchritt >= 2) {
            gewuenschteRichtung = richtungLetzterSchritt - 2;
        } else {
            gewuenschteRichtung = richtungLetzterSchritt + 2;
        }
        return gewuenschteRichtung;

    }

    /**
     * Schaut ob auf 4 der letzten 5 Feldern K�rner liegen und geht zur�ck um
     * diese zu nehemen.
     */
    void ueberpruefeWegAufKoerner() {
        if (memory.genuegendKoerner()) {
            schrittZurueck(5);
        }
    }

    /*
     * Pr�ft das Sichtfeld auf spezielle Anordnungen der Mauern
     * und reagiert auf diese entsprechend.
     *
     */
    void pruefeSichtfeld() {

        //linksseitige Ecke & Tunnel
        if (scannedTerritorium[1][2] == 1) {
            if (scannedTerritorium[3][2] == -1) {
                meinRechtsUm();
                meinVorUndSpeichern();
                if (scannedTerritorium[3][2] == -1) {
                    meinRechtsUm();
                }
            } else {
                schrittZurueck(1);
            }
        } //rechtsseitige Ecke
        else if (scannedTerritorium[3][2] == 1) {
            meinLinksUm();
            meinVorUndSpeichern();
            if (scannedTerritorium[1][2] == -1) {
                meinLinksUm();
            }
        } //Schlangenlinien
        else {
            if (letzteRichtung == Richtung.Links) {
                if (scannedTerritorium[3][2] == -1) {
                    meinRechtsUm();
                    meinVorUndSpeichern();
                    if (scannedTerritorium[3][2] == -1) {
                        meinRechtsUm();
                    }
                }
            } else if (letzteRichtung == Richtung.Rechts) {
                if (scannedTerritorium[1][2] == -1) {
                    meinLinksUm();
                    meinVorUndSpeichern();
                    if (scannedTerritorium[1][2] == -1) {
                        meinLinksUm();
                    }
                }
            }

        }

    }

    /*
     * Wenn auf den letzten 10 besuchten Feldern keine K�rner mehr liegen,
     * soll der Hamster zuf�llig eine andere Richtung einschlagen. Wir er-
     * hoffen uns davon, dass �ber eine Zufallskomponente alle Felder ein-
     * mal besucht werden. Zumindest aber soll so verhindert werden, dass
     * immer der selbe leere Bereich abgesucht wird, wenn es noch Bereiche
     * mit K�rnern gibt.
     * !memory.pruefeKoernerAufStrecke ergibt true, wenn die letzten 10 Felder leer
     * waren.
     */
    void ueberpruefeLetzte10Felder() {

        if (!memory.pruefeKoernerAufStrecke()) {
            // Finde zuf�llige Richtung und drehe in diese.
            dreheHamsterInRichtung(findeZufaelligeRichtung());
            meinVorUndSpeichern();

            // Setze die Information �ber leere Kacheln auf den letzten 10 Feldern zur�ck.
            // Wenn man das nicht macht, hat er m�glicherweise beim n�chsten Feld schon wieder 10 leere...
            memory.initKoernerArray();
        }

    }

    /**
     * Finde eine zuf�llige, aber m�gliche Drehrichtung
     */
    Richtung findeZufaelligeRichtung() {
        /*
         * Jetzt soll aus den Richtungen links, geradeaus und rechts zuf�llig
         * eine ausgew�hlt werden. Es kommen jedoch nur die Richtungen in die
         * Auswahl, bei denen der Hamster anschlie�end nicht direkt vor einer
         * Mauer stehen w�rde.
         * Zur�ck ist zun�chst keine Option, da ja genau dort zuletzt auch
         * keine K�rner mehr lagen. Die Wahrscheinlichkeit ist also bei links,
         * geradeaus und rechts h�her.
         */

        // Initialisiere Array infrage kommender Drehrichtungen.
        ArrayList<Richtung> drehrichtungen = new ArrayList();

        // Pr�fe infrage kommende Werte.
        if (!istMauerDa(Richtung.Links)) {
            drehrichtungen.add(Richtung.Links);
        }

        if (!istMauerDa(Richtung.Geradeaus)) {
            drehrichtungen.add(Richtung.Geradeaus);
        }

        if (!istMauerDa(Richtung.Rechts)) {
            drehrichtungen.add(Richtung.Rechts);
        }

        // Fall 0: Es wurden gar keine m�glichen Drehrichtungen gefunden.
        // Dann: Drehe um und gehe zur�ck.
        if (drehrichtungen.size() == 0) {
            // DEBUG von hier
            //super.schreib("Keine m�glichen Bewegungen nach vorne gefunden.");
            // DEBUG bis hier

            return Richtung.Zurueck;
        }

        // Fall 1: Es wurden nur eine m�gliche Drehrichtung gefunden.
        // Dann: W�hle diese Richtung.
        if (drehrichtungen.size() == 1) {
            // DEBUG von hier
            //super.schreib("Nur " + drehrichtungen.get(0).toString() + " m�glich.");
            // DEBUG bis hier

            return drehrichtungen.get(0);
        }

        // Fall 2: Es wurden mehrere m�gliche Drehrichtungen gefunden.
        // Dann: W�hle per Zufall eine dieser Richtungen aus.
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

            //super.schreib("RAND: M�glich w�ren " + listString + "w�hle " + drehrichtungen.get(index).toString() + ".");
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

        // Bei Geradeaus m�ssen wir nichts tun
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
     * Pr�ft, ob der Hamster ausgehend von der aktuellen Blickrichtung bei einer
     * Drehung in Richtung richtung vor einer Mauer stehen w�rde. Falls das
     * nicht entscheidbar ist (z.B. f�r zur�ck) wird false, also keine Mauer
     * zur�ck- gegeben.
     *
     * @param richtung Richtung f�r die geschaut werden soll. Muss Element des
     * ENUM Richtung sein.
     */
    boolean istMauerDa(Richtung richtung) {

        // Pr�fe f�r die gegebene Richtung, ob eine Mauer da ist.
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
        int gesamteKoerner = gefundeneKoerner + zurueckgelasseneKoerner;
        int effizienz = Math.round(((float) gefundeneKoerner / (float) gesamteKoerner) * (float) 100);
        int restladung = akku.gibLadung();
        super.schreib("Von " + gesamteKoerner + " K�rnern hat der Hamster " + gefundeneKoerner + " gefunden.\nZur�ckgelassen wurden " + zurueckgelasseneKoerner + " K�rner. Das entspricht einer Effizienz von " + effizienz + "%.\nDie Restakkuladung sind " + restladung + " Einheiten.");
    }

    /**
     * Reinigt das Feld mit dem Hamster
     */
    void reinige() {
        scannedTerritorium = SichtfeldScanner.scanSichtfeld(super.getReihe(), super.getSpalte(), super.getBlickrichtung());
        while (akku.hatLadung()) {
            // Pr�fe nun, ob damit alle K�rner in dem Territorium aufgesammelt sind.
            if (Territorium.getAnzahlKoerner() == 0) {
                break;
            }
            // Durch eine Drehung ist jetzt m�glicherweise der Akku leer geworden, deswegen neu pr�fen
            if (akku.hatLadung()) {
                ueberpruefeWegAufKoerner();
                ueberpruefeLetzte10Felder();

                if (!istMauerDa(Richtung.Geradeaus)) {
                 	// Es ist jetzt sicher nach vorne zu ziehen
                    // "Labyrinth-Modus"
                    if (scannedTerritorium[2][0] == -1 && ((scannedTerritorium[1][2] == 1 && scannedTerritorium[1][1] == -1 && scannedTerritorium[1][0] == 1) || (scannedTerritorium[3][2] == 1 && scannedTerritorium[3][1] == -1 && scannedTerritorium[3][0] == 1))) {
                        meinVorUndSpeichern();
                        dreheHamsterInRichtung(findeZufaelligeRichtung());
                    } else {
                        meinVorUndSpeichern();
                    }
                } else {
                    pruefeSichtfeld();
                }
            }
        }

        // Wenn wir hier angelangt sind, sind wir aus der while-Schleife geflogen.
        // Das passiert, wenn der Akku leer ist, oder alle K�rner gefunden wurden.
        zeigeEffizienz();
    }
}
