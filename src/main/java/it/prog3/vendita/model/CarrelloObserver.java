package it.prog3.vendita.model;

/**
 * Riceve gli aggiornamenti inviati da un carrello quando cambia il suo
 * contenuto.
 */
public interface CarrelloObserver {

    /**
     * Aggiorna lo stato dell'osservatore usando il contenuto corrente del
     * carrello.
     * @param carrello carrello che ha inviato la notifica
     */
    void aggiorna(Carrello carrello);

}
