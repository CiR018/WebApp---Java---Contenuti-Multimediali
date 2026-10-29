package it.prog3.vendita.exception;

/**
 * Segnala che la quantità disponibile di un prodotto non è sufficiente per
 * soddisfare una richiesta.
 */
public class ScortaInsufficienteException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea l'eccezione con un messaggio che descrive la carenza di scorta.
     *
     * @param messaggio descrizione dell'errore
     */
    public ScortaInsufficienteException(String messaggio) {
        super(messaggio);
    }
}
