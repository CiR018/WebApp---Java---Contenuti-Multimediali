package it.prog3.vendita.model;

/**
 * Associa un prodotto alla quantità richiesta nel carrello.
 */
public class RigaCarrello {
    private final Prodotto prodotto;
    private int quantita;

    /**
     * Crea una riga del carrello per il prodotto e la quantità indicati.
     *
     * @param prodotto prodotto associato alla riga
     * @param quantita quantità richiesta, maggiore di zero
     * @throws IllegalArgumentException se il prodotto è {@code null} o la
     *         quantità non è positiva
     */
    public RigaCarrello(Prodotto prodotto, int quantita) {
        if (prodotto == null) {
            throw new IllegalArgumentException("Il prodotto della riga non può essere null.");
        }
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità della riga deve essere maggiore di zero.");
        }
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    /**
     * Restituisce il prodotto associato alla riga.
     *
     * @return prodotto della riga
     */
    public Prodotto getProdotto() {
        return prodotto;
    }

    /**
     * Restituisce la quantità richiesta del prodotto.
     *
     * @return quantità positiva
     */
    public int getQuantita() {
        return quantita;
    }

    /**
     * Aggiorna la quantità richiesta del prodotto.
     *
     * @param quantita nuova quantità, maggiore di zero
     * @throws IllegalArgumentException se la quantità non è positiva
     */
    public void setQuantita(int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità della riga deve essere maggiore di zero.");
        }
        this.quantita = quantita;
    }
}
