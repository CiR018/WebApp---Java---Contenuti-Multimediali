package it.prog3.vendita.dao;

import it.prog3.vendita.model.Prodotto;
import java.sql.SQLException;
import java.util.List;

/**
 * Definisce le operazioni di persistenza disponibili per i prodotti.
 * L'interfaccia è implementata dal DAO SQLite e usata dai servizi senza
 * dipendere da una specifica classe di accesso al database.
 */
public interface ProdottoDAO {

    /**
     * Cerca un prodotto tramite l'identificativo assegnato dal database.
     *
     * @param id identificativo del prodotto
     * @return prodotto trovato, oppure {@code null} se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    Prodotto trovaPerID(long id) throws SQLException;

    /**
     * Recupera tutti i prodotti da mostrare nel catalogo.
     *
     * @return prodotti trovati; lista vuota se non ci sono prodotti
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    List<Prodotto> trovaTutti() throws SQLException;

    /**
     * Cerca un prodotto tramite il suo codice univoco.
     *
     * @param codice codice del prodotto
     * @return prodotto trovato, oppure {@code null} se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    Prodotto trovaPerCodice(String codice) throws SQLException;

    /**
     * Salva un nuovo prodotto e assegna all'oggetto l'ID generato dal database.
     *
     * @param prodotto prodotto da salvare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void salva(Prodotto prodotto) throws SQLException;

    /**
     * Aggiorna i dati di un prodotto già salvato.
     *
     * @param prodotto prodotto con i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void aggiorna(Prodotto prodotto) throws SQLException;

    /**
     * Elimina il prodotto con l'identificativo indicato.
     *
     * @param id identificativo del prodotto da eliminare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void elimina(long id) throws SQLException;

    /**
     * Recupera i prodotti associati alla categoria indicata.
     *
     * @param idCategoria identificativo della categoria
     * @return prodotti trovati; lista vuota se la categoria non contiene prodotti
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    List<Prodotto> trovaPerCategoria(long idCategoria) throws SQLException;
}
