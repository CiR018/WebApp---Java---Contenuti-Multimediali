package it.prog3.vendita.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta il carrello temporaneo di un visitatore o di un utente e ne
 * gestisce le righe e gli observer registrati.
 */
public class Carrello {
    private final List<RigaCarrello> righe;
    private final List<CarrelloObserver> osservatori;

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
        RigaCarrello rigaTrovata = null;

        if (prodotto == null) {
            throw new IllegalArgumentException("Il prodotto non può essere null.");
        }
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
        }

        for (RigaCarrello riga : righe) {
            if (riga.getProdotto().getId() == prodotto.getId()) {
                rigaTrovata = riga;
                break;
            }
        }
        if (rigaTrovata == null) {
            righe.add(new RigaCarrello(prodotto, quantita));
        } else {
            int nuovaQuantita = rigaTrovata.getQuantita() + quantita;
            rigaTrovata.setQuantita(nuovaQuantita);
        }

        notificaOsservatori();
    }

    /**
     * Rimuove dal carrello la riga associata al prodotto e notifica gli
     * observer.
     *
     * @param prodotto prodotto da rimuovere dal carrello
     * @throws IllegalArgumentException se il prodotto è {@code null} oppure
     *         non è presente nel carrello
     */
    public void rimuoviProdotto(Prodotto prodotto) {
        RigaCarrello rigaTrovata = null;

        if (prodotto == null) {
            throw new IllegalArgumentException("Il prodotto non può essere null.");
        }

        for (RigaCarrello riga : righe) {
            if (riga.getProdotto().getId() == prodotto.getId()) {
                rigaTrovata = riga;
                break;
            }
        }

        if (rigaTrovata == null) {
            throw new IllegalArgumentException("Il prodotto non è presente nel carrello.");
        }
        righe.remove(rigaTrovata);

        notificaOsservatori();
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
        return List.copyOf(righe);
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

        for (RigaCarrello riga : righe) {
            BigDecimal subtotale = riga.getProdotto().getPrezzoScontato()
                    .multiply(BigDecimal.valueOf(riga.getQuantita()));
            totale = totale.add(subtotale);
        }
        return totale;
    }

    /**
     * Registra un observer che dovrà ricevere le notifiche quando il carrello
     * cambia. Se l'osservatore è già registrato non lo aggiunge.
     *
     * @param osservatore observer da registrare
     * @throws IllegalArgumentException se l'observer è {@code null} o è già
     *         stato registrato
     */
    public void registraOsservatore(CarrelloObserver osservatore) {
        if (osservatore == null) {
            throw new IllegalArgumentException("L'observer non può essere null.");
        }
        if (!osservatori.contains(osservatore)) {
            osservatori.add(osservatore);
        } else {
            throw new IllegalArgumentException("L'observer è già registrato.");
        }
    }

    /**
     * Rimuove un observer dalla lista di quelli registrati.
     *
     * @param osservatore observer da rimuovere
     * @throws IllegalArgumentException se l'observer è {@code null} o non è
     *         registrato
     */
    public void rimuoviOsservatore(CarrelloObserver osservatore) {
        if (osservatore == null) {
            throw new IllegalArgumentException("L'observer da rimuovere non può essere null.");
        }
        if (!osservatori.remove(osservatore)) {
            throw new IllegalArgumentException("L'observer da rimuovere non è registrato.");
        }
    }

    /**
     * Notifica gli observer registrati, passando a ciascuno lo stato corrente
     * di questo carrello.
     */
    public void notificaOsservatori() {
        for (CarrelloObserver osservatore : List.copyOf(osservatori)) {
            osservatore.aggiorna(this);
        }
    }
}
