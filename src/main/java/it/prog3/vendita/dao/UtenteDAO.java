package it.prog3.vendita.dao;

import it.prog3.vendita.model.Utente;
import java.sql.SQLException;

/**
 * Definisce le operazioni di persistenza necessarie per gli account.
 */
public interface UtenteDAO {

    /**
     * Cerca un account tramite il suo identificativo.
     * 
     * @param id identificativo dell'account
     * @return account trovato, oppure null se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    public Utente trovaPerID(long id) throws SQLException;

    /**
     * Cerca un account tramite username.
     * 
     * @param username username da cercare
     * @return account trovato, oppure null se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    public Utente trovaPerUsername(String username) throws SQLException;

    /**
     * Salva un nuovo account e assegna all'oggetto l'ID generato dal database.
     * 
     * @param utente account da salvare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    public void salva(Utente utente) throws SQLException;

    /**
     * Aggiorna nel database i dati di un account già esistente.
     * Al momento non è usato da nessun caso d'uso: è stato predisposto
     * per una possibile funzione futura di modifica del profilo, con cui
     * l'utente potrà cambiare nome, cognome, username o password.
     *
     * @param utente account con i dati aggiornati, deve avere un ID valido
     * @throws IllegalArgumentException se l'utente è null o non ha un ID valido
     * @throws SQLException             se l'aggiornamento fallisce o non modifica
     *                                  esattamente un account
     */
    public void aggiorna(Utente utente) throws SQLException;
}
