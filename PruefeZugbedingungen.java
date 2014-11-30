/**
 * Ermöglicht eine Prüfung von Vorbedingungen
 */
import de.hamster.debugger.model.Territorium;import de.hamster.debugger.model.Territory;import de.hamster.model.HamsterException;import de.hamster.model.HamsterInitialisierungsException;import de.hamster.model.HamsterNichtInitialisiertException;import de.hamster.model.KachelLeerException;import de.hamster.model.MauerDaException;import de.hamster.model.MaulLeerException;import de.hamster.model.MouthEmptyException;import de.hamster.model.WallInFrontException;import de.hamster.model.TileEmptyException;import de.hamster.debugger.model.Hamster;class PruefeZugbedingungen {

	/**
	 * Prüft, ob noch Akkuladung für mindestens einen Zug und Körner vorhanden sind.
	 * @param akku Die Akku-Instanz des Hamsters
	 * @returns true, wenn Zug möglich, false ansonsten.
	 */
    public static boolean istZugMoeglich(Akku akku) {
        return (akku.hatLadung() && Territorium.getAnzahlKoerner() > 0);
    }
}
