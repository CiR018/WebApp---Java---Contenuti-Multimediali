package it.prog3.vendita.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il carrello temporaneo di un visitatore o di un utente e ne
 * gestisce le righe e gli observer registrati.
 */
public class Carrello {
    private List<RigaCarrello> righe;
    private List<CarrelloObserver> osservatori;

    /**
     * Crea un carrello vuoto, pronto a contenere prodotti e observer.
     */
    public Carrello() {
        righe = new ArrayList<>();
        osservatori = new ArrayList<>();
    }

    /**
     * Aggiunge al carrello la quantità richiesta di un prodotto. Se il prodotto
     * è già presente, la quantità della riga corrispondente viene incrementata;
     * al termine dell'operazione gli observer vengono notificati.
     *
     * @param prodotto prodotto da aggiungere
     * @param quantita quantità da aggiungere, maggiore di zero
     * @throws IllegalArgumentException se il prodotto è {@code null} o la
     *         quantità non è positiva
     */
    public void aggiungiProdotto(Prodotto prodotto, int quantita) {

    }

    /**
     * Rimuove dal carrello la riga associata al prodotto. Se la riga viene
     * rimossa, gli observer vengono notificati.
     *
     * @param prodotto prodotto da rimuovere dal carrello
     */
    public void rimuoviProdotto(Prodotto prodotto) {

    }

    /**
     * Restituisce le righe attualmente presenti nel carrello. La lista
     * restituita non deve consentire di modificare la struttura interna del
     * carrello.
     *
     * @return lista non modificabile delle righe; vuota se il carrello non
     *         contiene prodotti
     */
    public List<RigaCarrello> getRighe() {

    }

    /**
     * Calcola il totale del carrello sommando il prezzo scontato di ogni
     * prodotto moltiplicato per la quantità della relativa riga.
     *
     * @return totale corrente del carrello, pari a {@link BigDecimal#ZERO} se
     *         non contiene righe
     */
    public BigDecimal getTotale() {
        BigDecimal totale = BigDecimal.ZERO;

        for(RigaCarrello riga : righe) {
            BigDecimal subtotale = riga.getProdotto().getPrezzoScontato().multiply(BigDecimal.valueOf(riga.getQuantita()));

            totale = totale.add(subtotale);
        }
       return totale;
    }

    /**
     * Registra un observer che dovrà ricevere le notifiche quando il carrello
     * cambia.
     *
     * @param osservatore observer da registrare
     */
    public void registraOsservatore(CarrelloObserver osservatore) {

    }

    /**
     * Rimuove un observer dalla lista di quelli registrati.
     *
     * @param osservatore observer da rimuovere
     */
    public void rimuoviOsservatore(CarrelloObserver osservatore) {

    }

    /**
     * Notifica gli observer registrati, passando a ciascuno lo stato corrente
     * di questo carrello.
     */
    public void notificaOsservatori() {

    }
}
