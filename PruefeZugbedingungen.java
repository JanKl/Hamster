/**
 * Erm�glicht eine Pr�fung von Vorbedingungen
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class PruefeZugbedingungen {

	/**
	 * Pr�ft, ob noch Akkuladung f�r mindestens einen Zug und K�rner vorhanden sind.
	 * @param akku Die Akku-Instanz des Hamsters
	 * @returns true, wenn Zug m�glich, false ansonsten.
	 */
    public static boolean istZugMoeglich(Akku akku) {
        return (akku.hatLadung() && Territorium.getAnzahlKoerner() > 0);
    }
}
