package it.prog3.vendita.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.prog3.vendita.model.Ruolo;
import it.prog3.vendita.model.Utente;

/**
 * Implementazione di UtenteDAO che salva e legge gli account dal database SQLite.
 * Ogni metodo apre una nuova connessione tramite ConnessioneDB e la chiude
 * alla fine dell'operazione. Le query usano PreparedStatement per evitare
 * problemi di SQL injection.
 */
public class UtenteDAOSQLite implements UtenteDAO {

    private final ConnessioneDB connessioneDB;

    /**
     * Crea il DAO usando la connessione al database passata come parametro.
     *
     * @param connessione oggetto che fornisce le connessioni al database
     * @throws IllegalArgumentException se la connessione è null
     */
    public UtenteDAOSQLite(ConnessioneDB connessione) {
        if (connessione == null)
            throw new IllegalArgumentException("ConnessioneDB non può essere null. \n ");

        this.connessioneDB = connessione;
    }

    /**
     * Cerca un account nel database tramite il suo ID.
     *
     * @param id identificativo dell'account, deve essere maggiore di zero
     * @return l'account trovato, oppure null se non esiste
     * @throws IllegalArgumentException se l'ID non è positivo
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    @Override
    public Utente trovaPerID(long id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("L'ID dell'utente deve essere positivo: " + id);
        }

        String query = """
                SELECT id, nome, cognome, username, passwordHash, ruolo
                        FROM utente
                            WHERE id = ?
                        """;
        try (Connection connessione = connessioneDB.getConnection();
                PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet risultato = statement.executeQuery()) {
                // next() è false quando la query non ha trovato alcun utente.
                if (!risultato.next())
                    return null;

                return mappaRiga(risultato);
            }
        }
    }

    /**
     * Cerca un account nel database tramite lo username.
     * Viene usato durante il login e per controllare se uno username è già occupato.
     *
     * @param username username da cercare
     * @return l'account trovato, oppure null se non esiste
     * @throws IllegalArgumentException se lo username è null o vuoto
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    @Override
    public Utente trovaPerUsername(String username) throws SQLException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Username dell'utente deve essere inserito e non vuoto: " + username + "\n");
        }

        String query = """
                SELECT id, nome, cognome, username, passwordHash, ruolo
                        FROM utente
                            WHERE username = ?
                        """;
        try (Connection connessione = connessioneDB.getConnection();
                PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet risultato = statement.executeQuery()) {
                // next() è false quando la query non ha trovato alcun utente.
                if (!risultato.next())
                    return null;

                return mappaRiga(risultato);
            }
        }
    }

    /**
     * Inserisce un nuovo account nel database e assegna all'oggetto
     * l'ID generato da SQLite.
     *
     * @param utente account da salvare, non deve avere già un ID
     * @throws IllegalArgumentException se l'utente è null o ha già un ID
     * @throws SQLException se l'inserimento fallisce, per esempio se lo username esiste già
     */
    @Override
    public void salva(Utente utente) throws SQLException {
        if (utente == null) {
            throw new IllegalArgumentException("Utente non può essere null.");
        }

        if (utente.getId() != 0) {
            throw new IllegalArgumentException("Utente ha già un ID assegnato e non è nuovo.");
        }

        String query = """
                INSERT INTO Utente
                    (nome, cognome, username, passwordHash, ruolo)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connessione = connessioneDB.getConnection();
                PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, utente.getNome().trim());
            statement.setString(2, utente.getCognome().trim());
            statement.setString(3, utente.getUsername());
            statement.setString(4, utente.getPasswordHash());
            statement.setString(5, utente.getRuolo().name());

            int righeInserite = statement.executeUpdate();
            if (righeInserite != 1)
                throw new SQLException("L'inserimento non ha salvato esattamente un utente. \n");

            // L'ID va letto dalla stessa connessione che ha eseguito l'INSERT.
            try (Statement statementId = connessione.createStatement();
                    ResultSet risultatoId = statementId.executeQuery("SELECT last_insert_rowid()")) {
                // recupera l'id generato dal database
                if (!risultatoId.next()) {
                    throw new SQLException("SQLite non ha restituito l'ID dell'utente inserito.");
                }
                utente.setId(risultatoId.getLong(1));
            }
        }
    }

    /**
     * Aggiorna nel database i dati di un account già esistente.
     * Al momento non è usato da nessun caso d'uso: è stato predisposto
     * per una possibile funzione futura di modifica del profilo, con cui
     * l'utente potrà cambiare nome, cognome, username o password.
     *
     * @param utente account con i dati aggiornati, deve avere un ID valido
     * @throws IllegalArgumentException se l'utente è null o non ha un ID valido
     * @throws SQLException se l'aggiornamento fallisce o non modifica esattamente un account
     */
    @Override
    public void aggiorna(Utente utente) throws SQLException {
        if (utente == null) {
            throw new IllegalArgumentException("Utente non può essere null.");
        }

        if (utente.getId() <= 0) {
            throw new IllegalArgumentException("Utente deve avere ID già assegnato per aggiornare i suoi dati. \n");
        }

        String query = """
                UPDATE  Utente
                    SET nome = ?, cognome = ?, username = ?, passwordHash = ?, ruolo = ?
                WHERE id = ?
                """;

        try (Connection connessione = connessioneDB.getConnection();
                PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, utente.getNome().trim());
            statement.setString(2, utente.getCognome().trim());
            statement.setString(3, utente.getUsername());
            statement.setString(4, utente.getPasswordHash());
            statement.setString(5, utente.getRuolo().name());
            // setto ID
            statement.setLong(6, utente.getId());

            int righeAggiornate = statement.executeUpdate();
            if (righeAggiornate != 1)
                throw new SQLException("L'aggiornamento non ha modificato esattamente un utente. \n");
        }
    }

    /**
     * Converte la riga corrente del ResultSet in un oggetto Utente.
     * Serve a non ripetere lo stesso codice in tutti i metodi di ricerca.
     *
     * @param risultato risultato della query, posizionato sulla riga da leggere
     * @return l'utente costruito con i dati della riga
     * @throws SQLException se una colonna non può essere letta
     */
    private Utente mappaRiga(ResultSet risultato) throws SQLException {

        long idUtente = risultato.getLong("id");
        String nome = risultato.getString("nome");
        String cognome = risultato.getString("cognome");
        String username = risultato.getString("username");
        String passwordHash = risultato.getString("passwordHash");
        Ruolo ruolo = Ruolo.valueOf(risultato.getString("ruolo"));

        Utente utente = new Utente(nome, cognome, username, passwordHash, ruolo);
        utente.setId(idUtente);

        return utente;
    }
}
