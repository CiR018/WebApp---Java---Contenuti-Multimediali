package it.prog3.vendita.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un ordine effettuato da un utente.
 * Contiene la data di creazione, le righe acquistate e il totale calcolato
 * a partire dai subtotali delle righe. Gli importi sono espressi in euro.
 */
public class Ordine {
    private long id;
    private LocalDateTime dataOrdine;
    private Utente utente;
    private BigDecimal totale;
    private List<RigaOrdine> righe;
   /*private StatoOrdine stato;
    private TipoPagamento metodoPagamento;*/

    /**
     * Crea un ordine associato all'utente indicato, registra la data corrente
     * e inizializza la lista delle righe come vuota.
     *
     * @param utente utente proprietario dell'ordine
     * @throws IllegalArgumentException se {@code utente} è {@code null}
     */
    public Ordine(Utente utente) {
        
        if(utente != null) 
            this.utente = utente;
        else 
            throw new IllegalArgumentException("Utente non deve essere vuoto " + utente + "\n");
        
        this.dataOrdine = LocalDateTime.now();
        this.righe = new ArrayList<>();
        this.totale = BigDecimal.ZERO;
    }


     /**
     * Assegna all'ordine l'identificativo generato dal database.
     * L'ID può essere assegnato una sola volta e deve essere positivo.
     *
     * @param id identificativo positivo restituito dal database
     * @throws IllegalArgumentException se {@code id} è minore o uguale a zero
     * @throws IllegalStateException se a questo ordine è già stato assegnato un ID
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe ordine è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe ordine deve essere positivo !");
        this.id = id;
    }
    /**
     * Restituisce la data e l'ora in cui è stato creato l'ordine.
     *
     * @return data e ora di creazione
     */
    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    /**
     * Restituisce l'identificativo assegnato dal database, oppure zero se
     * l'ordine non è ancora stato salvato.
     *
     * @return identificativo dell'ordine
     */
    public long getId() {
        return id;
    }
    /*  public TipoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }*/
    /**
     * Restituisce una copia non modificabile delle righe attualmente associate
     * all'ordine. Per aggiungere righe usare {@link #aggiungiRiga(RigaOrdine)}.
     *
     * @return copia non modificabile delle righe dell'ordine
     */
    public List<RigaOrdine> getRighe() {
        return List.copyOf(righe);
    }
    /*public StatoOrdine getStato() {
        return stato;
    }*/
    /**
     * Restituisce il totale corrente dell'ordine, espresso in euro.
     * Un ordine senza righe ha totale pari a zero.
     *
     * @return totale corrente dell'ordine
     */
    public BigDecimal getTotale() {
        return totale;
    }
    /**
     * Restituisce l'utente proprietario dell'ordine.
     *
     * @return utente associato all'ordine
     */
    public Utente getUtente() {
        return utente;
    }

  /*   public void setMetodoPagamento(TipoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    
    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    */
    /**
     * Aggiunge una riga all'ordine e ricalcola il totale.
     * La riga deve essere non nulla e fare riferimento a questo stesso ordine.
     *
     * @param riga riga da aggiungere all'ordine
     * @throws IllegalArgumentException se {@code riga} è {@code null} o appartiene
     *                                  a un ordine diverso
     */
    public void aggiungiRiga(RigaOrdine riga) {
        if (riga == null) {
            throw new IllegalArgumentException("La riga dell'ordine non può essere null.");
        }
        if (riga.getOrdine() != this) {
            throw new IllegalArgumentException("La riga deve appartenere a questo ordine.");
        }

        righe.add(riga);
        calcolaTotale();
    }

    /**
     * Calcola il totale sommando i subtotali delle righe presenti e aggiorna
     * il campo {@code totale}. Se la lista è vuota, il totale diventa zero.
     *
     */
    public void calcolaTotale() {
        BigDecimal accumulatore = BigDecimal.ZERO;

        for (RigaOrdine rigaOrdine : righe) {
            accumulatore = accumulatore.add(rigaOrdine.getSubtotale());
        }
        totale = accumulatore;
    }

}
