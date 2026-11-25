package it.prog3.vendita.dao;

import java.sql.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.prog3.vendita.model.CategoriaComponente;
import it.prog3.vendita.model.Prodotto;
import it.prog3.vendita.model.Ruolo;
import it.prog3.vendita.model.Utente;

public class UtenteDAOSQLite implements UtenteDAO {

    final ConnessioneDB connessioneDB;

    public UtenteDAOSQLite(ConnessioneDB connessione) {
        if (connessione == null)
            throw new IllegalArgumentException("ConnessioneDB non può essere null. \n ");

        this.connessioneDB = connessione;
    }

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
                // next() è false quando la query non ha trovato alcun prodotto.
                if (!risultato.next())
                    return null;

                return mappaRiga(risultato);
            }
        }
    }

    /**
     * Cerca un account tramite username.
     * 
     * @param username username da cercare
     * @return account trovato, oppure null se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
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
                // next() è false quando la query non ha trovato alcun prodotto.
                if (!risultato.next())
                    return null;

                return mappaRiga(risultato);
            }
        }
    }

    /**
     * Salva un nuovo account e assegna all'oggetto l'ID generato dal database.
     * 
     * @param utente account da salvare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
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
     * Aggiorna i dati di un account già salvato.
     * 
     * @param utente account con i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
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
                throw new SQLException("L'inserimento non ha aggiornato esattamente un utente. \n");
        }
    }

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
