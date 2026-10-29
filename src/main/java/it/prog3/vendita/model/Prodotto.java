package it.prog3.vendita.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.prog3.vendita.exception.ScortaInsufficienteException;

/**
 * Rappresenta un contenuto multimediale venduto nel catalogo.
 *
 * <p>L'identificativo è assegnato dal database una sola volta. Il codice deve
 * essere univoco: la sua unicità tra prodotti diversi è verificata dal livello
 * di persistenza.</p>
 */
public class Prodotto {

    private long id;
    private String codice;
    private String nome;
    private String descrizione;
    private int quantitaScorta;
    private BigDecimal costo;
    private Integer percentualeSconto;
    private CategoriaComponente categoria;

    /**
     * Crea un prodotto non ancora identificato dal database.
     *
     * @param nome nome obbligatorio del prodotto
     * @param codice codice obbligatorio del prodotto
     * @param descrizione descrizione obbligatoria del prodotto
     * @param quantitaScorta quantità iniziale in magazzino, non negativa
     * @param costo costo unitario, non nullo e non negativo
     * @param percentualeSconto sconto percentuale tra 1 e 99, oppure
     *        {@code null} se non è previsto uno sconto
     * @param categoria categoria obbligatoria del prodotto
     * @throws IllegalArgumentException se uno dei parametri non rispetta i
     *         vincoli indicati
     */
    public Prodotto(String nome, String codice, String descrizione, int quantitaScorta, BigDecimal costo, Integer percentualeSconto, CategoriaComponente categoria) {
        this.nome = validaTesto(nome, "Il nome");
        this.codice = validaTesto(codice, "Il codice");
        this.descrizione = validaTesto(descrizione, "La descrizione");
        setQuantitaScorta(quantitaScorta);
        setCosto(costo);
        setPercentualeSconto(percentualeSconto);
        setCategoria(categoria);
    }

    /**
     * Assegna l'identificativo positivo generato dal database. L'operazione è
     * consentita una sola volta per prodotto.
     *
     * @param id identificativo generato dal database
     * @throws IllegalArgumentException se {@code id} non è positivo
     * @throws IllegalStateException se l'identificativo è già stato assegnato
     */
    public void setId(long id) {
        if (this.id != 0) {
            throw new IllegalStateException("L'ID del prodotto è già stato assegnato dal database.");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID del prodotto deve essere positivo.");
        }
        this.id = id;
    }

    /**
     * Aggiorna il nome del prodotto.
     *
     * @param nome nuovo nome, non nullo né vuoto
     * @throws IllegalArgumentException se il nome è nullo o vuoto
     */
    public void setNome(String nome) {
        this.nome = validaTesto(nome, "Il nome");
    }

    /**
     * Aggiorna la descrizione del prodotto.
     *
     * @param descrizione nuova descrizione, non nulla né vuota
     * @throws IllegalArgumentException se la descrizione è nulla o vuota
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = validaTesto(descrizione, "La descrizione");
    }

    /**
     * Aggiorna la quantità disponibile in magazzino.
     *
     * @param quantitaScorta nuova quantità, non negativa
     * @throws IllegalArgumentException se la quantità è negativa
     */
    public void setQuantitaScorta(int quantitaScorta) {
        if (quantitaScorta < 0) {
            throw new IllegalArgumentException("La quantità in scorta non può essere negativa.");
        }
        this.quantitaScorta = quantitaScorta;
    }

    /**
     * Aggiorna il costo unitario del prodotto.
     *
     * @param costo nuovo costo, non nullo e non negativo
     * @throws IllegalArgumentException se il costo è nullo o negativo
     */
    public void setCosto(BigDecimal costo) {
        if (costo == null || costo.signum() < 0) {
            throw new IllegalArgumentException("Il costo non può essere null o negativo.");
        }
        this.costo = costo;
    }

    /**
     * Aggiorna lo sconto percentuale del prodotto.
     *
     * @param percentualeSconto valore da 1 a 99, oppure {@code null} per
     *        rimuovere lo sconto
     * @throws IllegalArgumentException se il valore non nullo non è compreso
     *         tra 1 e 99
     */
    public void setPercentualeSconto(Integer percentualeSconto) {
        if (percentualeSconto != null
                && (percentualeSconto < 1 || percentualeSconto > 99)) {
            throw new IllegalArgumentException("La percentuale di sconto deve essere compresa tra 1 e 99.");
        }
        this.percentualeSconto = percentualeSconto;
    }

    /**
     * Aggiorna la categoria del prodotto.
     *
     * @param categoria nuova categoria, non nulla
     * @throws IllegalArgumentException se la categoria è {@code null}
     */
    public void setCategoria(CategoriaComponente categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria del prodotto non può essere null.");
        }
        this.categoria = categoria;
    }

    /**
     * Restituisce l'identificativo assegnato dal database.
     *
     * @return ID del prodotto, oppure {@code 0} prima del salvataggio
     */
    public long getId() {
        return id;
    }

    /**
     * Restituisce il codice del prodotto.
     *
     * @return codice del prodotto
     */
    public String getCodice() {
        return codice;
    }

    /**
     * Restituisce la categoria del prodotto.
     *
     * @return categoria associata al prodotto
     */
    public CategoriaComponente getCategoria() {
        return categoria;
    }

    /**
     * Restituisce il costo unitario non scontato.
     *
     * @return costo del prodotto
     */
    public BigDecimal getCosto() {
        return costo;
    }

    /**
     * Restituisce la descrizione del prodotto.
     *
     * @return descrizione del prodotto
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return nome del prodotto
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce la percentuale di sconto applicata, se presente.
     *
     * @return percentuale tra 1 e 99, oppure {@code null} se non c'è uno sconto
     */
    public Integer getPercentualeSconto() {
        return percentualeSconto;
    }

    /**
     * Calcola il prezzo unitario dopo l'eventuale sconto. Il risultato viene
     * arrotondato a due cifre decimali usando {@link RoundingMode#HALF_UP}.
     *
     * @return prezzo scontato, o costo originale se non è previsto uno sconto
     */
    public BigDecimal getPrezzoScontato() {
        BigDecimal prezzo = costo;
        if (percentualeSconto != null) {
            prezzo = costo.multiply(BigDecimal.valueOf(100 - percentualeSconto))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return prezzo.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Restituisce la quantità disponibile in magazzino.
     *
     * @return quantità disponibile, non negativa
     */
    public int getQuantitaScorta() {
        return quantitaScorta;
    }

    /**
     * Verifica se il magazzino contiene la quantità richiesta.
     *
     * @param quantita quantità richiesta
     * @return {@code true} se la quantità richiesta è positiva e disponibile;
     *         {@code false} altrimenti
     */
    public boolean isDisponibile(int quantita) {
        return quantita > 0 && quantitaScorta >= quantita;
    }

    /**
     * Riduce la scorta della quantità prelevata.
     *
     * @param quantita quantità da prelevare, maggiore di zero
     * @throws IllegalArgumentException se la quantità non è positiva
     * @throws ScortaInsufficienteException se la scorta non copre la quantità
     *         richiesta
     */
    public void riduciScorta(int quantita) throws ScortaInsufficienteException {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità da prelevare deve essere maggiore di zero.");
        }
        if (!isDisponibile(quantita)) {
            throw new ScortaInsufficienteException("Scorta insufficiente: richiesti " + quantita + ", disponibili " + quantitaScorta + ".");
        }
        quantitaScorta -= quantita;
    }

    /**
     * Verifica che una stringa obbligatoria contenga testo e ne rimuove gli
     * spazi iniziali e finali.
     *
     * @param valore testo da controllare
     * @param nomeCampo nome del campo usato nel messaggio d'errore
     * @return valore ripulito dagli spazi esterni
     * @throws IllegalArgumentException se il valore è nullo o vuoto
     */
    private static String validaTesto(String valore, String nomeCampo) {
        if (valore == null || valore.isBlank()) {
            throw new IllegalArgumentException(nomeCampo + " non può essere nullo o vuoto.");
        }
        return valore.trim();
    }
}
