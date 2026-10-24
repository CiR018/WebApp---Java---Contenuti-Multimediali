package it.prog3.vendita.model;

import java.math.BigDecimal;

/**
 * Classe che rappresenta uan riga di ordine effettuata dall'utente, esempio utente acquista 4 prodotti uguali, avremo 1 righe d'ordine riferite allo stesso Ordine con 4 di quantità
 * RigaOrdine
 */
public class RigaOrdine {

    private long id; 
    private Ordine ordine;
    private Prodotto prodotto;
    private int quantita;
    private BigDecimal prezzoAcquisto;

    /**
     * Costruttore riga ordine con controlli su ogni parametro
     * @param ordine Rappresenta l'ordine al quale e associata la riga
     * @param quantita Rappresenta il numero di prodotti richiesti e acquistati dall'utente
     * @param prodotto Rappresenta il prodotto acquistato
     * @param prezzoAcquisto Rappresenta il costo storico al quale il prodotto viene venduto in euro
     * @throws IllegalArgumentException per valori non consentiti causati da:<br>
     * - ordine non presente<br>
     * - prodotto non presente<br>
     * - quantita non positiva<br>
     * - prezzoAcquisto non presente o valore minore di zero<br>
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

    /** Restituisce l'id
     * @return long
     */
    public long getId() {
        return id;
    }
    /** Restituisce l'ordine di riferimento associato alla riga
     * @return Ordine
     */
    public Ordine getOrdine() {
        return ordine;
    }
    /** Restituisce il prodotto di riferimento associato all'ordine
     * @return Prodotto
     */
    public Prodotto getProdotto() {
        return prodotto;
    }
    /** Restituisce il prezzo di acquisto storico 
     * @return BigDecimal
     */
    public BigDecimal getPrezzoAcquisto() {
        return prezzoAcquisto;
    }
    /**
     * Restituisce la quantità di prodotti richiesti di quel prodotto
     * @return int
     */
    public int getQuantita() {
        return quantita;
    }
    /** Calcola il subtotale della riga prezzoAcquisto * quantita il tutto convertito in BigDecimal
     * @return BigDecimal
     */
    public BigDecimal getSubtotale() {
        return prezzoAcquisto.multiply(BigDecimal.valueOf(getQuantita())); //conversione di quantita in BigDecimal
    }

    /**
     * Il metodo set id viene utilizzato per assegnare un singolo ID da parte del database 
     * Una volta impostato dal database il metodo setId non sarà più utilizzabile<br>
     * Viene controllato se l'id interno della riga ordine sia stato modificato in caso di esito positivo lanciamo un'eccezione<br>
     * Se l'id passato al metodo e negativo o zero lanciamo un'errore.<br>
     * In tutti gli altri casi assegniamo l'id per la prima volta.<br>
     * nota: il metodo non deve permettere altri inserimenti dopo la prima modifica da parte del database
     * @param id identificativo del record nel DB
     * @throws IllegalArgumentException per gestire un id non positivo.
     * @throws IllegalStateException per gestire un valore non consentito per id e non più assegnabile.
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe riga ordine è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe riga ordine deve essere positivo !");
        this.id = id;
    }
}
