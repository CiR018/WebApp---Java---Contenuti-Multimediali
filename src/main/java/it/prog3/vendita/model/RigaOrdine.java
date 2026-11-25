package it.prog3.vendita.model;

import java.math.BigDecimal;

/**
 * Rappresenta un prodotto e la quantità acquistata in un ordine.
 */
public class RigaOrdine {

    private long id; 
    private Ordine ordine;
    private Prodotto prodotto;
    private int quantita;
    private BigDecimal prezzoAcquisto;

    /**
     * Crea una riga per il prodotto e la quantità indicati.
     * @param ordine ordine a cui appartiene la riga
     * @param quantita quantità acquistata
     * @param prodotto prodotto acquistato
     * @param prezzoAcquisto prezzo del prodotto al momento dell'acquisto
     * @throws IllegalArgumentException se un parametro non è valido
     */
    public RigaOrdine(Ordine ordine, Prodotto prodotto, int quantita, BigDecimal prezzoAcquisto) {
        if(ordine != null)
            this.ordine = ordine;
        else 
            throw new IllegalArgumentException("Ordine di riga ordine non inserito " + ordine + "\n");
        
        if(prodotto != null)
            this.prodotto = prodotto;
        else 
            throw new IllegalArgumentException("Prodotto di riga ordine non inserito " + prodotto + "\n");

        if(quantita > 0)
            this.quantita = quantita;
        else 
            throw new IllegalArgumentException("Quantita di riga ordine non positiva " + quantita + "\n");

        if(prezzoAcquisto != null && prezzoAcquisto.compareTo(BigDecimal.ZERO) > 0)    
            this.prezzoAcquisto = prezzoAcquisto;
        else
            throw new IllegalArgumentException("Prezzo acquisto vuoto oppure negativo " + prezzoAcquisto + "\n");
    }

    /**
     * Restituisce l'identificativo della riga.
     * @return identificativo assegnato dal database, oppure zero se non è ancora salvata
     */
    public long getId() {
        return id;
    }
    /**
     * Restituisce l'ordine a cui appartiene la riga.
     * @return ordine associato
     */
    public Ordine getOrdine() {
        return ordine;
    }
    /**
     * Restituisce il prodotto acquistato.
     * @return prodotto associato
     */
    public Prodotto getProdotto() {
        return prodotto;
    }
    /**
     * Restituisce il prezzo pagato al momento dell'acquisto.
     * @return prezzo storico del prodotto
     */
    public BigDecimal getPrezzoAcquisto() {
        return prezzoAcquisto;
    }
    /**
     * Restituisce la quantità acquistata.
     * @return quantità della riga
     */
    public int getQuantita() {
        return quantita;
    }
    /**
     * Calcola il subtotale della riga.
     * @return prezzo di acquisto moltiplicato per la quantità
     */
    public BigDecimal getSubtotale() {
        return prezzoAcquisto.multiply(BigDecimal.valueOf(getQuantita())); //conversione di quantita in BigDecimal
    }

    /**
     * Imposta l'identificativo generato dal database. Si può chiamare una sola volta.
     * @param id identificativo positivo della riga
     * @throws IllegalArgumentException se l'identificativo non è positivo
     * @throws IllegalStateException se l'identificativo è già stato impostato
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe riga ordine è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe riga ordine deve essere positivo !");
        this.id = id;
    }
}
