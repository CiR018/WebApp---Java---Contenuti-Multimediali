package it.prog3.vendita.model;

import java.math.BigDecimal;

/**
 *  La classe Prodotto viene utilizzata per rappresentare i prodotti presenti nel catalogo 
 *  del sito di contenuti multimediali.
 *  Per ogni prodotto abbiamo un id inserito dal database, codice del prodotto, nome, 
 *  descrizione e la quantità di scorte di quel prodotto, la percentuale di sconto se inserita. 
 * 
 */

public class Prodotto {
    private long id;        //assegnato dal database una sola volta tramite il DAO
    private String codice;  //codice identificativo del prodotto
    private String nome;
    private String descrizione;
    private int quantitaScorta;
    private BigDecimal costo;
    private Integer percentualeSconto;
    //private CategoriaComponente categoria;

    /**
     * Costruttore completo per creare un nuovo oggetto Prodotto.
     * @param codice il codice identificativo del prodotto
     * @param nome il nome del prodotto
     * @param descrizione una breve descrizione del prodotto
     * @param quantitaScorta il numero di scorte iniziali disponibili in magazzino
     * @param costo il prezzo del singolo prodotto
     * @param percentualeSconto la percentuale di sconto applicata (può essere null)
     */
    public Prodotto(String codice, String nome, String descrizione, int quantitaScorta, BigDecimal costo, Integer percentualeSconto) {

        this.codice = codice;
        this.nome = nome;
        this.descrizione = descrizione;
        this.quantitaScorta = quantitaScorta;
        this.costo = costo;
        this.percentualeSconto = percentualeSconto;
        //this.categoria = categoria;
    }

    /**
     * Il metodo set id viene utilizzato per assegnare un singolo ID da parte del database 
     *Una volta impostato dal database il metodo setId non sarà più utilizzabile<br>
     * Viene controllato se l'id interno del prodotto e già stato modificato se si lanciamo un'eccezione<br>
     * Se l'id passato al metodo e negativo o zero lanciamo un'errore.<br>
     * In tutti gli altri casi assegniamo l'id per la prima volta.<br>
     * nota: il metodo non deve permettere altri inserimenti dopo la prima modifica da parte del database
     * @param id identificativo del record nel DB
     *@throws IllegalArgumentException per gestire un id non positivo.
     *@throws IllegalStateException per gestire un valore non consentito per id.
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe prodotto è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe prodotto deve essere positivo !");
        this.id = id;
    }
    /** Setta il nome del prodotto
     * @param nome prodotto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    /**
    *Setta la descrizione del prodotto da aggiungere
     * @param descrizione del prodotto
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    /** Setta la quantità di prodotti disponibili
     * @param quantitaScorta numero di scorte da inserire per quel prodotto
     */
    public void setQuantitaScorta(int quantitaScorta) {
        this.quantitaScorta = quantitaScorta;
    }
    /** Setta il costo del singolo prodotto
     * @param costo setta il costo del singolo prodotto
     */
    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }
    /**
     * Setta la percentuale dello sconto disponibile su quel prodotto
     * @param percentualeSconto Setta la percentuale di sconto del prodotto (può essere NULL)
     */
    public void setPercentualeSconto(Integer percentualeSconto) {
        this.percentualeSconto = percentualeSconto;
    }
    /** Setta il codice identificativo del prodotto
     * @param codice inserimento del codice identificativo del prodotto
     */
    public void setCodice(String codice) {
        this.codice = codice;
    }

    /** Restituisce l'id impostato dal database 1 volta
     * @return id dell'identificativo a DB
     */
    public long getId() {
        return id;
    }
    /** Retituisce il codice identificativo del prodotto
     * @return restituisce il codice del prodotto a cui e associato
     */
    public String getCodice() {
        return codice;
    }
    /** Restituisce il costo del singolo prodotto
     * @return restituisce il prezzo del prodotto
     */
    public BigDecimal getCosto() {
        return costo;
    }
    /** Restituisce la descrizione de prodotto
     * @return Restituisce la descrizione del prodotto
     */
    public String getDescrizione() {
        return descrizione;
    }
    /** Restituisce il nome del prodotto
     * @return restituisce il nome del prodotto
     */
    public String getNome() {
        return nome;
    }
    /** Restituisce la percentuale del prodotto (può essere NULL)
     * @return resituisce la percentuale di sconto applicata a quel prodotto
     */
    public Integer getPercentualeSconto() {
        return percentualeSconto;
    }
    /** Restituisce il numero di prodotti attualmente disponibili
     * @return restituisce la quantità di scorte disponibili del prodotto
     */
    public int getQuantitaScorta() {
        return quantitaScorta;
    }

    /** Verifica la disponibilità se le scorte sono maggiore di zero <br>
    * maggiore di 0 true<br>
    * minore di  0 false 
    * @return true se ci sono scorte a disposizione, altrimenti false.
    */
    public boolean isDisponibile() {
        
        if(quantitaScorta > 0)
            return true;
        else 
            return false;
    }

    /**
    * Riduce le scorte disponibli in base alla disponibilità in base al numero di prodotti prelevati
    * @param prodottiDaPrelevare indica i prodotti richiesti
    * @throws IllegalArgumentException se il prodotto non e disponibile nelle quantità richieste
    */
    public void riduciScorta(int prodottiDaPrelevare) {
        if(prodottiDaPrelevare > 0 && isDisponibile() && this.quantitaScorta >= prodottiDaPrelevare )
            this.quantitaScorta -= prodottiDaPrelevare;
        else
            throw new IllegalArgumentException("I prodotti disponibili sono insufficienti ! \n" +  "Quantita disponibili: " + this.quantitaScorta + "\n");
        //!altrimenti inserire un errore..
    }

}

