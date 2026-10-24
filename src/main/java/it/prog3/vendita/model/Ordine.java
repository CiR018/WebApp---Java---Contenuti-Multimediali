package it.prog3.vendita.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Ordine {
    private long id;
    private LocalDateTime dataOrdine;
    private Utente utente;
    private BigDecimal totale;
    private List<RigaOrdine> righe;
   /*private StatoOrdine stato;
    private TipoPagamento metodoPagamento;*/

    /*public Ordine(LocalDateTime dataOrdine, Utente utente, BigDecimal totale, RigaOrdine riga, StatoOrdine stato, TipoPagamento metodoPagamento) {
        this.dataOrdine = 
        this.utente = utente;
    }*/


     /** 
     * Il metodo set id viene utilizzato per assegnare un singolo ID da parte del database 
     *Una volta impostato dal database il metodo setId non sarà più utilizzabile<br>
     * Viene controllato se l'id interno di Ordine sia stato modificato se si lanciamo un'eccezione<br>
     * Se l'id passato al metodo e negativo o zero lanciamo un'errore.<br>
     * In tutti gli altri casi assegniamo l'id per la prima volta.<br>
     * nota: il metodo non deve permettere altri inserimenti dopo la prima modifica da parte del database
     * @param id identificativo del record nel DB
     * @throws IllegalArgumentException per gestire un id non positivo.
     * @throws IllegalStateException per gestire un valore non consentito per id.
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe ordine è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe ordine deve essere positivo !");
        this.id = id;
    }
    public LocalDateTime getDataOrdine() {
        return dataOrdine;
    }

    public long getId() {
        return id;
    }
   /*  public TipoPagamento getMetodoPagamento() {
        return metodoPagamento;
    }*/
    public List<RigaOrdine> getRighe() {
        return righe;
    }
    /*public StatoOrdine getStato() {
        return stato;
    }*/
    public BigDecimal getTotale() {
        return totale;
    }
    public Utente getUtente() {
        return utente;
    }

  /*   public void setMetodoPagamento(TipoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }
    public void setRighe(List<RigaOrdine> righe) {
        this.righe = righe;
    }
    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }
    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    */

}
