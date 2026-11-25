package it.prog3.vendita.dao;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import it.prog3.vendita.model.CategoriaComponente;
import it.prog3.vendita.model.CategoriaComposita;
import it.prog3.vendita.model.CategoriaFoglia;




/**
 * Implementazione SQLite di CategoriaDAO.
 * Ricostruisce la gerarchia delle categorie dal riferimento id_padre:
 * le categorie senza figli diventano CategoriaFoglia, quelle con figli
 * diventano CategoriaComposita. Ogni operazione usa una connessione
 * distinta; le modifiche ricorsive usano una transazione per mantenere coerenti
 * i dati persistiti.
 */
public class CategoriaDAOSQLite implements CategoriaDAO {

    /**
     * Fornitore di connessioni al database (dipendenza iniettata).
     */
    private final ConnessioneDB connessioneDB;

    /**
     * Crea il DAO con la sorgente di connessioni indicata.
     * @param connessioneDB fornitore di connessioni, non null
     * @throws IllegalArgumentException se connessioneDB è null
     */
    public CategoriaDAOSQLite(ConnessioneDB connessioneDB) {

        if(connessioneDB == null)
            throw new IllegalArgumentException("ConnessioneDB non può essere null.");

       this.connessioneDB = connessioneDB;
    }

    /**
     * Cerca una categoria per ID e ne ricostruisce l'intero sottoalbero.
     * @param id ID della categoria, deve essere positivo
     * @return la categoria con il suo sottoalbero, oppure null se l'ID non esiste
     * @throws IllegalArgumentException se id non è positivo
     * @throws SQLException se l'accesso al database fallisce
     */
    @Override
    public CategoriaComponente trovaPerID(long id) throws SQLException {
        if (id <= 0)
            throw new IllegalArgumentException("L'ID della categoria deve essere positivo ! " + id + "\n");


        String query = """
            SELECT id, nome
                    FROM Categoria
                        WHERE id = ?
                    """;
        String nome;

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet risultato = statement.executeQuery()) {
                // next() è false quando la query non ha trovato alcuna categoria.
                if(!risultato.next())
                    return null;

                nome = risultato.getString("nome");
            }
        }

        List<Long> idFigli = new ArrayList<>();

        String queryFigli = """
            SELECT id
                    FROM Categoria
                            WHERE id_padre = ?
                        """;

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(queryFigli)) {
            statement.setLong(1, id);
            try (ResultSet risultato = statement.executeQuery()) {
                while(risultato.next())
                    idFigli.add(risultato.getLong("id"));
            }
        }


        if (idFigli.isEmpty()) {
            CategoriaFoglia categoriaFoglia = new CategoriaFoglia(nome);
            categoriaFoglia.setId(id);
            return categoriaFoglia;
        }
        else {
            CategoriaComposita composita = new CategoriaComposita(nome);
            for (long idFiglio : idFigli)
                composita.aggiungi(trovaPerID(idFiglio));

            composita.setId(id);
            return composita;
        }
    }

    /**
     * Restituisce le categorie radice presenti nel database.
     * @return lista delle categorie radice con i rispettivi sottoalberi; vuota
     * se non ci sono categorie
     * @throws SQLException se l'accesso al database fallisce
     */
    @Override
    public List<CategoriaComponente> trovaTutte() throws SQLException {
        List<CategoriaComponente> categorie = new ArrayList<>();

        String query = """
            SELECT id
                FROM Categoria
                    WHERE id_padre IS NULL
            """;

        List<Long> idCategorie = new ArrayList<>();

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    idCategorie.add(risultato.getLong("id"));
                }
            }
        }

        for (long id : idCategorie) {
            categorie.add(trovaPerID(id));
        }
        return categorie;
    }

    /**
     * Salva una nuova categoria insieme a tutto il suo sottoalbero.
     * @param categoria categoria nuova da salvare, con ID non ancora assegnato
     * @throws IllegalArgumentException se la categoria è null o se lei
     * (o una sua sottocategoria) ha già un ID
     * @throws SQLException se l'accesso al database fallisce
     */
    @Override
    public void salva(CategoriaComponente categoria) throws SQLException {
        if (categoria == null)
            throw new IllegalArgumentException("La categoria da salvare non può essere vuota! ");

        if (categoria.getID() != 0)
            throw new IllegalArgumentException("La categoria ha già un id assegnato quindi non può essere salvata! " + categoria.getID() + "\n");


        try (Connection connessione = connessioneDB.getConnection()) {
            connessione.setAutoCommit(false); //il commit resta in sospeso
            try {
                salvaConPadre(connessione, categoria, null); //radice quindi non ha padre
                connessione.commit(); //conferma tutto
            } catch(SQLException | RuntimeException e) {
                connessione.rollback(); //torna allo stato precedente
                throw e;
            }
        }


    }

    /**
     * Inserisce una categoria e, ricorsivamente, le sue sottocategorie.
     * @param connessione connessione già aperta della transazione in corso
     * @param categoria categoria da inserire, con ID non ancora assegnato
     * @param idPadre ID del padre già salvato, oppure null per una radice
     * @throws IllegalArgumentException se la categoria ha già un ID
     * @throws SQLException se l'inserimento o la lettura dell'ID falliscono
     */
    private void salvaConPadre(Connection connessione, CategoriaComponente categoria, Long idPadre) throws SQLException {
        if (categoria.getID() != 0)
            throw new IllegalArgumentException("L'id deve essere nuovo e non ancora assegnato! " + categoria.getID() + "\n");

        String query = "INSERT INTO Categoria (nome, id_padre) VALUES (?, ?)";

        try (PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, categoria.getNome());
            if (idPadre == null)
                statement.setNull(2, Types.INTEGER);
            else
                statement.setLong(2, idPadre);

            int righeInserite = statement.executeUpdate();

            if (righeInserite != 1)
                throw new SQLException("L'inserimento non ha salvato esattamente una categoria. \n");

            long idNuovo;

            try (Statement statementId = connessione.createStatement(); ResultSet risultatoId = statementId.executeQuery("SELECT last_insert_rowid()")) {
                 if (!risultatoId.next()) {
                    throw new SQLException("SQLite non ha restituito l'ID della categoria salvata. \n");
                }
                categoria.setId(risultatoId.getLong(1));
                idNuovo = risultatoId.getLong(1);
            }
            if (categoria instanceof CategoriaComposita composita) {
                 for (CategoriaComponente figlio : composita.getFigli())
                    salvaConPadre(connessione, figlio, idNuovo);
            }
        }
    }
    /**
     * Aggiorna il nome di una categoria esistente e visita ricorsivamente i
     * suoi figli.
     * I figli già persistiti vengono aggiornati mantenendo il loro ID; i
     * nuovi figli, identificati da ID 0, vengono inseriti collegandoli
     * al padre corrente. Tutte le modifiche sono eseguite in un'unica
     * transazione. Questo metodo non sposta categorie già persistite tra padri
     * e non rimuove figli assenti dall'albero ricevuto.
     * Se la transazione fallisce dopo che un nuovo nodo ha ricevuto l'ID, il
     * rollback ripristina il database ma non azzera l'ID già assegnato
     * all'oggetto Java.
     * @param categoria radice del sottoalbero da aggiornare; deve avere un ID
     * positivo
     * @throws IllegalArgumentException se la categoria è null, non ha
     * un ID positivo o un nuovo sottoalbero contiene nodi già identificati
     * @throws SQLException se il database non contiene uno dei nodi da
     * aggiornare o si verifica un errore durante la transazione
     */
    @Override
    public void aggiorna(CategoriaComponente categoria) throws SQLException {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria non può essere null.");
        }
        if (categoria.getID() <= 0) {
            throw new IllegalArgumentException(
                    "La categoria da aggiornare deve avere un ID positivo.");
        }

        validaNuoviSottoalberi(categoria, false);

        try (Connection connessione = connessioneDB.getConnection()) {
            connessione.setAutoCommit(false);
            try {
                aggiornaRicorsivo(connessione, categoria);
                connessione.commit();
            } catch (SQLException | RuntimeException eccezione) {
                try {
                    connessione.rollback();
                } catch (SQLException erroreRollback) {
                    eccezione.addSuppressed(erroreRollback);
                }
                throw eccezione;
            }
        }
    }

    /**
     * Aggiorna il nodo corrente e persiste ricorsivamente i suoi figli usando
     * la connessione della transazione chiamante.
     * @param connessione connessione già aperta della transazione
     * @param categoria categoria esistente da aggiornare
     * @throws SQLException se il nodo non esiste o un'operazione SQL fallisce
     */
    private void aggiornaRicorsivo(
            Connection connessione,
            CategoriaComponente categoria) throws SQLException {

        String query = """
                UPDATE Categoria
                SET nome = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, categoria.getNome());
            statement.setLong(2, categoria.getID());

            int righeAggiornate = statement.executeUpdate();
            if (righeAggiornate != 1) {
                throw new SQLException(
                        "Nessuna categoria trovata con ID " + categoria.getID());
            }
        }

        if (categoria instanceof CategoriaComposita composita) {
            for (CategoriaComponente figlio : composita.getFigli()) {
                if (figlio.getID() == 0) {
                    salvaConPadre(connessione, figlio, categoria.getID());
                } else {
                    aggiornaRicorsivo(connessione, figlio);
                }
            }
        }
    }

    /**
     * Verifica che un ramo nuovo non contenga categorie già persistite.
     * Un nodo nuovo può essere figlio di un nodo esistente, ma tutti i suoi
     * discendenti devono essere nuovi perché il loro ID padre verrà assegnato
     * durante l'inserimento ricorsivo.
     * @param categoria nodo da controllare
     * @param padreNuovo true se il padre del nodo non è ancora persistito
     * @throws IllegalArgumentException se un nuovo ramo contiene un ID assegnato
     */
    private void validaNuoviSottoalberi(
            CategoriaComponente categoria,
            boolean padreNuovo) {

        if (categoria == null) {
            throw new IllegalArgumentException("Una categoria figlia non può essere null.");
        }

        long id = categoria.getID();
        if (id < 0 || (padreNuovo && id != 0)) {
            throw new IllegalArgumentException(
                    "Un nuovo sottoalbero deve contenere solo categorie con ID 0.");
        }

        if (categoria instanceof CategoriaComposita composita) {
            for (CategoriaComponente figlio : composita.getFigli()) {
                validaNuoviSottoalberi(figlio, id == 0);
            }
        }
    }

    /**
     * Elimina una categoria e tutte le sue sottocategorie.
     * Le categorie vengono eliminate dal basso verso l'alto in un'unica
     * transazione. Se una categoria del sottoalbero è referenziata da un
     * prodotto, il vincolo di chiave esterna impedisce la cancellazione e
     * l'intera transazione viene annullata.
     * @param id ID positivo della categoria radice del sottoalbero da eliminare
     * @throws IllegalArgumentException se id non è positivo
     * @throws SQLException se la categoria non esiste, un prodotto la
     * referenzia o si verifica un errore durante l'accesso ai dati
     */
    @Override
    public void elimina(long id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID della categoria deve essere positivo.");
        }

        try (Connection connessione = connessioneDB.getConnection()) {
            connessione.setAutoCommit(false);
            try {
                eliminaRicorsivo(connessione, id);
                connessione.commit();
            } catch (SQLException | RuntimeException eccezione) {
                try {
                    connessione.rollback();
                } catch (SQLException erroreRollback) {
                    eccezione.addSuppressed(erroreRollback);
                }
                throw eccezione;
            }
        }
    }

    /**
     * Elimina prima i figli e poi la categoria indicata, usando la connessione
     * della transazione chiamante.
     * @param connessione connessione già aperta della transazione
     * @param id identificativo della categoria da eliminare
     * @throws SQLException se la categoria non esiste o un vincolo impedisce
     * la cancellazione
     */
    private void eliminaRicorsivo(Connection connessione, long id) throws SQLException {
        List<Long> idFigli = new ArrayList<>();
        String queryFigli = "SELECT id FROM Categoria WHERE id_padre = ?";

        try (PreparedStatement statement = connessione.prepareStatement(queryFigli)) {
            statement.setLong(1, id);
            try (ResultSet risultato = statement.executeQuery()) {
                while (risultato.next()) {
                    idFigli.add(risultato.getLong("id"));
                }
            }
        }

        for (long idFiglio : idFigli) {
            eliminaRicorsivo(connessione, idFiglio);
        }

        String queryElimina = "DELETE FROM Categoria WHERE id = ?";
        try (PreparedStatement statement = connessione.prepareStatement(queryElimina)) {
            statement.setLong(1, id);
            int righeEliminate = statement.executeUpdate();
            if (righeEliminate != 1) {
                throw new SQLException("Nessuna categoria trovata con ID " + id);
            }
        }
    }
}
