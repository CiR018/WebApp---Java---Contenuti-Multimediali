package it.prog3.vendita.dao;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Fornisce le connessioni al database SQLite dell'applicazione.
 */
public class ConnessioneDB {

    /**
     * Istanza condivisa della classe di configurazione.
     */
    private static final ConnessioneDB ISTANZA = new ConnessioneDB();

    /**
     * URL JDBC del file SQLite usato dall'applicazione.
     */
    private final String URL;

    /**
     * Crea la configurazione di connessione.
     */
        ConnessioneDB() {
        Path percorsoDatabase = Path.of(System.getProperty("user.dir"),"DB","vendita.db");
        
        URL = "jdbc:sqlite:" + percorsoDatabase.toAbsolutePath();
        }

    /**
     * Restituisce l'istanza condivisa di questa classe.
     * @return istanza condivisa che contiene la configurazione SQLite
     */
    public static ConnessioneDB getInstance() {
        return ISTANZA;
    }

    /**
     * Apre una nuova connessione al database SQLite e abilita i controlli delle
     * chiavi esterne per quella connessione.
     * @return nuova connessione JDBC al database configurato
     * @throws SQLException se non è possibile aprire la connessione o attivare
     * il controllo delle chiavi esterne
     */
    public Connection getConnection() throws SQLException {
        Connection connessione = DriverManager.getConnection(URL);

        try (Statement statement = connessione.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            return connessione;
        } catch(SQLException eccezione) {
            connessione.close();
            throw eccezione;
        }
    }

}
