package it.prog3.vendita.dao;

import it.prog3.vendita.model.Ordine;
import java.sql.SQLException;
import java.util.List;

/**
 * Definisce le operazioni di persistenza disponibili per gli ordini.
 */
public interface OrdineDAO {

    /**
     * Cerca un ordine tramite il suo identificativo.
     *
     * @param id identificativo dell'ordine
     * @return ordine trovato, oppure {@code null} se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    Ordine trovaPerID(long id) throws SQLException;

    /**
     * Recupera gli ordini appartenenti all'utente indicato.
     *
     * @param idUtente identificativo dell'utente proprietario
     * @return ordini trovati; lista vuota se l'utente non ne ha
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    List<Ordine> trovaPerUtente(long idUtente) throws SQLException;

    /**
     * Recupera tutti gli ordini, per le operazioni autorizzate di amministrazione.
     *
     * @return ordini trovati; lista vuota se non ce ne sono
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    List<Ordine> trovaTutti() throws SQLException;

    /**
     * Aggiorna un ordine già salvato.
     *
     * @param ordine ordine con i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void aggiorna(Ordine ordine) throws SQLException;

    /**
     * Salva un nuovo ordine.
     *
     * @param ordine ordine da salvare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void salva(Ordine ordine) throws SQLException;
}
