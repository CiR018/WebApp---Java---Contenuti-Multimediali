package it.prog3.vendita.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import it.prog3.vendita.model.CategoriaComponente;
import it.prog3.vendita.model.Prodotto;

/**
 * Implementazione SQLite delle operazioni di persistenza dei prodotti.
 *
 * <p>Usa {@link ConnessioneDB} per ottenere una connessione per ogni
 * operazione e {@link CategoriaDAO} per ricostruire la categoria associata
 * ai prodotti letti dal database. Il costo è memorizzato in centesimi nel
 * database e rappresentato in euro nel modello.</p>
 */
public class ProdottoDAOSQLite implements ProdottoDAO {

    /** Fornisce una nuova connessione SQLite per ciascuna operazione. */
    private final ConnessioneDB connessioneDB;

    /** Recupera gli oggetti categoria associati ai prodotti. */
    private final CategoriaDAO categoriaDAO;

    /**
     * Crea il DAO SQLite dei prodotti.
     *
     * @param connessioneDB configurazione usata per aprire le connessioni
     * @param categoriaDAO DAO usato per recuperare le categorie dei prodotti
     * @throws IllegalArgumentException se una dipendenza è {@code null}
     */
    public ProdottoDAOSQLite(ConnessioneDB connessioneDB, CategoriaDAO categoriaDAO) {

        if (connessioneDB == null) {
            throw new IllegalArgumentException("ConnessioneDB non può essere null.");
        }
        if (categoriaDAO == null) {
            throw new IllegalArgumentException("CategoriaDAO non può essere null.");
        }

        this.connessioneDB = connessioneDB;
        this.categoriaDAO = categoriaDAO;
    }
 

    /**
     * Cerca un prodotto tramite l'ID assegnato dal database.
     *
     * @param id identificativo positivo del prodotto
     * @return prodotto trovato, oppure {@code null} se non esiste
     * @throws IllegalArgumentException se l'ID non è positivo
     * @throws SQLException se si verifica un errore SQL o manca la categoria
     *         riferita dal prodotto
     */
    @Override
    public Prodotto trovaPerID(long id) throws SQLException {
        
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID del prodotto deve essere positivo: " + id);
        }

        String query = """ 
            SELECT id, codice, nome, descrizione, quantitaScorta,
                costo, percentualeSconto, id_categoria
                    FROM Prodotto
                        WHERE id = ? 
                    """;
        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet risultato = statement.executeQuery()) {
                // next() è false quando la query non ha trovato alcun prodotto.
                if(!risultato.next())
                    return null;

                // Legge i dati della riga trovata e li converte nei tipi del modello.
                long idProdotto = risultato.getLong("id");
                String codice = risultato.getString("codice");
                String nome = risultato.getString("nome");
                String descrizione = risultato.getString("descrizione");
                int quantitaScorta = risultato.getInt("quantitaScorta");
                BigDecimal costoInCentesimi = risultato.getBigDecimal("costo");
                // Il database conserva i centesimi; il modello usa euro.
                BigDecimal costoInEuro = costoInCentesimi.movePointLeft(2);
                Integer percentualeScontoLetta =  risultato.getInt("percentualeSconto");
                // getInt restituisce 0 anche per SQL NULL: wasNull distingue i due casi.
                Integer percentualeSconto = risultato.wasNull() ? null : percentualeScontoLetta;
                
                long idCategoria = risultato.getLong("id_categoria");
                // Il prodotto conserva l'oggetto categoria, non solo il suo ID.
                CategoriaComponente categoria = categoriaDAO.trovaPerID(idCategoria);

                if(categoria == null)
                    throw new SQLException("Categoria inesistente per quel prodotto \n");

                // Il costruttore crea il prodotto; l'ID letto viene assegnato dopo.
                Prodotto prodotto = new Prodotto(nome, codice, descrizione, quantitaScorta, costoInEuro, percentualeSconto, categoria);
                prodotto.setId(idProdotto);

                return prodotto;
            }
        } 
    }
    /**
     * Recupera tutti i prodotti presenti nel catalogo.
     *
     * @return prodotti trovati, oppure lista vuota se il catalogo è vuoto
     * @throws SQLException se si verifica un errore SQL o manca la categoria
     *         riferita da un prodotto
     */
    @Override
    public List<Prodotto> trovaTutti() throws SQLException {
        List<Prodotto> prodotti = new ArrayList<>();

        String query = """ 
            SELECT id, codice, nome, descrizione, quantitaScorta,
                costo, percentualeSconto, id_categoria
                    FROM Prodotto
                    """;
        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            try (ResultSet risultato = statement.executeQuery()) {
                // Qui il cursore avanza su ogni prodotto trovato.
                while(risultato.next()) {
                    long idProdotto = risultato.getLong("id");
                    String codice = risultato.getString("codice");
                    String nome = risultato.getString("nome");
                    String descrizione = risultato.getString("descrizione");
                    int quantitaScorta = risultato.getInt("quantitaScorta");
                    BigDecimal costoInCentesimi = risultato.getBigDecimal("costo");
                    // Il database conserva i centesimi; il modello usa euro.
                    BigDecimal costoInEuro = costoInCentesimi.movePointLeft(2);
                    Integer percentualeScontoLetta =  risultato.getInt("percentualeSconto");
                    // getInt restituisce 0 anche per SQL NULL: wasNull distingue i due casi.
                    Integer percentualeSconto = risultato.wasNull() ? null : percentualeScontoLetta;
                    
                    long idCategoria = risultato.getLong("id_categoria");
                    CategoriaComponente categoria = categoriaDAO.trovaPerID(idCategoria);

                    if(categoria == null)
                        throw new SQLException("Categoria inesistente per quel prodotto \n");

                    Prodotto prodotto = new Prodotto(nome, codice, descrizione, quantitaScorta, costoInEuro, percentualeSconto, categoria);
                    prodotto.setId(idProdotto);
                    prodotti.add(prodotto);
                } 
            }
        } 
        return prodotti;
    }
    /**
     * Cerca un prodotto tramite il suo codice univoco.
     *
     * @param codiceProdotto codice da cercare; gli spazi esterni vengono rimossi
     * @return prodotto trovato, oppure {@code null} se non esiste
     * @throws IllegalArgumentException se il codice è {@code null}, vuoto o
     *         composto soltanto da spazi
     * @throws SQLException se si verifica un errore SQL o manca la categoria
     *         riferita dal prodotto
     */
    @Override
    public Prodotto trovaPerCodice(String codiceProdotto) throws SQLException {
                
        if (codiceProdotto == null || codiceProdotto.isBlank()) {
            throw new IllegalArgumentException("Il codice del prodotto non può essere nullo o vuoto.");
        }

        String query = """ 
            SELECT id, codice, nome, descrizione, quantitaScorta,
                costo, percentualeSconto, id_categoria
                    FROM Prodotto
                        WHERE codice = ? 
                    """;
        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, codiceProdotto.trim());
            try (ResultSet risultato = statement.executeQuery()) {
                // Il codice è univoco, quindi la ricerca restituisce al massimo una riga.
                if(!risultato.next())
                    return null;

                long idProdotto = risultato.getLong("id");
                String codice = risultato.getString("codice");
                String nome = risultato.getString("nome");
                String descrizione = risultato.getString("descrizione");
                int quantitaScorta = risultato.getInt("quantitaScorta");
                BigDecimal costoInCentesimi = risultato.getBigDecimal("costo");
                // Il database conserva i centesimi; il modello usa euro.
                BigDecimal costoInEuro = costoInCentesimi.movePointLeft(2);
                Integer percentualeScontoLetta =  risultato.getInt("percentualeSconto");
                // getInt restituisce 0 anche per SQL NULL: wasNull distingue i due casi.
                Integer percentualeSconto = risultato.wasNull() ? null : percentualeScontoLetta;
                
                long idCategoria = risultato.getLong("id_categoria");
                CategoriaComponente categoria = categoriaDAO.trovaPerID(idCategoria);

                if(categoria == null)
                    throw new SQLException("Categoria inesistente per quel prodotto \n");

                Prodotto prodotto = new Prodotto(nome, codice, descrizione, quantitaScorta, costoInEuro, percentualeSconto, categoria);
                prodotto.setId(idProdotto);

                return prodotto;
            }
        } 

    }
    /**
     * Inserisce un nuovo prodotto e assegna all'oggetto l'ID generato da SQLite.
     *
     * @param prodotto prodotto nuovo da salvare
     * @throws IllegalArgumentException se il prodotto è {@code null}, ha già
     *         un ID o la sua categoria non ha ancora un ID
     * @throws ArithmeticException se il costo non è rappresentabile esattamente
     *         in centesimi
     * @throws SQLException se l'inserimento o il recupero dell'ID fallisce
     */
    @Override
    public void salva(Prodotto prodotto) throws SQLException {
        
        if (prodotto == null) {
            throw new IllegalArgumentException("Il prodotto non può essere null.");
        }

        if (prodotto.getId() != 0) {
            throw new IllegalArgumentException("Il prodotto ha già un ID assegnato e non è nuovo.");
        }

        long idCategoria = prodotto.getCategoria().getID();
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("La categoria deve avere un ID assegnato dal database.");
        }

        String query = """
            INSERT INTO Prodotto
                (codice, nome, descrizione, quantitaScorta, costo,
                percentualeSconto, id_categoria)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, prodotto.getCodice().trim());
            statement.setString(2, prodotto.getNome().trim());
            statement.setString(3, prodotto.getDescrizione());
            statement.setInt(4, prodotto.getQuantitaScorta());
            // Converte gli euro del modello in centesimi interi per SQLite.
            long costoInCentesimi = prodotto.getCosto().movePointRight(2).longValueExact();
            statement.setLong(5, costoInCentesimi);

            if (prodotto.getPercentualeSconto() == null)
                statement.setNull(6, Types.INTEGER);
            else 
                statement.setInt(6, prodotto.getPercentualeSconto());
            

            statement.setLong(7, idCategoria);

            int righeInserite = statement.executeUpdate();
            if (righeInserite != 1) 
                throw new SQLException("L'inserimento non ha salvato esattamente un prodotto. \n");
            
            // L'ID va letto dalla stessa connessione che ha eseguito l'INSERT.
            try (Statement statementId = connessione.createStatement(); ResultSet risultatoId = statementId.executeQuery("SELECT last_insert_rowid()")) {
                //recupera l'id generato dal database
                    if (!risultatoId.next()) {
                        throw new SQLException("SQLite non ha restituito l'ID del prodotto inserito.");
                    }

                    prodotto.setId(risultatoId.getLong(1));
            }
        }
    }
    /**
     * Aggiorna nel database i dati di un prodotto già identificato.
     *
     * @param prodotto prodotto con i dati aggiornati e un ID assegnato
     * @throws IllegalArgumentException se il prodotto è {@code null}, non ha
     *         un ID positivo o la categoria non ha un ID positivo
     * @throws ArithmeticException se il costo non è rappresentabile esattamente
     *         in centesimi
     * @throws SQLException se si verifica un errore SQL o l'ID non corrisponde
     *         a un prodotto presente
     */
    @Override
    public void aggiorna(Prodotto prodotto) throws SQLException {
                
        if (prodotto == null) {
            throw new IllegalArgumentException("Il prodotto non può essere null.");
        }

        if (prodotto.getId() <= 0) {
            throw new IllegalArgumentException("Il prodotto non ha un ID assegnato.");
        }

        long idCategoria = prodotto.getCategoria().getID();
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("La categoria deve avere un ID assegnato dal database.");
        }

        String query = """
            UPDATE Prodotto
                SET codice = ?, nome = ?, descrizione = ?, quantitaScorta = ?,
                    costo = ?, percentualeSconto = ?, id_categoria = ?
                WHERE id = ?
            """;

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setString(1, prodotto.getCodice().trim());
            statement.setString(2, prodotto.getNome().trim());
            statement.setString(3, prodotto.getDescrizione());
            statement.setInt(4, prodotto.getQuantitaScorta());
            // Converte gli euro del modello in centesimi interi per SQLite.
            long costoInCentesimi = prodotto.getCosto().movePointRight(2).longValueExact();
            statement.setLong(5, costoInCentesimi);

            if (prodotto.getPercentualeSconto() == null)
                statement.setNull(6, Types.INTEGER);
            else 
                statement.setInt(6, prodotto.getPercentualeSconto());
            

            statement.setLong(7, idCategoria);
            statement.setLong(8, prodotto.getId());

            int righeAggiornate = statement.executeUpdate();
            if (righeAggiornate != 1) {
                throw new SQLException("L'aggiornamento non ha modificato esattamente un prodotto.");
            }
        }
    }

    /**
     * Elimina dal database il prodotto con l'ID indicato.
     *
     * @param id identificativo positivo del prodotto da eliminare
     * @throws IllegalArgumentException se l'ID non è positivo
     * @throws SQLException se si verifica un errore SQL, il prodotto non esiste
     *         o un vincolo del database impedisce l'eliminazione
     */
    @Override
    public void elimina(long id) throws SQLException {

        if (id <= 0) {
            throw new IllegalArgumentException("L'ID del prodotto deve essere positivo.");
        }

        String query = """
            DELETE
                FROM PRODOTTO
                    WHERE id = ?
            """;

        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
            statement.setLong(1, id);

            int righeEliminate = statement.executeUpdate();
            if (righeEliminate != 1) {
                throw new SQLException("L'eliminazione non ha rimosso esattamente un prodotto.");
            }
        }
    }
    /**
     * Recupera i prodotti assegnati direttamente alla categoria indicata.
     *
     * @param idCategoria identificativo positivo della categoria
     * @return prodotti della categoria, oppure lista vuota se non ce ne sono
     * @throws IllegalArgumentException se l'ID categoria non è positivo
     * @throws SQLException se si verifica un errore SQL o manca la categoria
     *         riferita da un prodotto
     */
    @Override
    public List<Prodotto> trovaPerCategoria(long idCategoria) throws SQLException {
           List<Prodotto> prodotti = new ArrayList<>();

        if(idCategoria <= 0)
            throw new IllegalArgumentException("L'id della categoria associata al prodotto deve essere positivo " + idCategoria + "\n" );

        String query = """ 
            SELECT id, codice, nome, descrizione, quantitaScorta,
                costo, percentualeSconto, id_categoria
                    FROM Prodotto
                        WHERE id_categoria = ?
                    """;
        try (Connection connessione = connessioneDB.getConnection(); PreparedStatement statement = connessione.prepareStatement(query)) {
             statement.setLong(1, idCategoria);
            try (ResultSet risultato = statement.executeQuery()) {
            
                while(risultato.next()) {
                    long idProdotto = risultato.getLong("id");
                    String codice = risultato.getString("codice");
                    String nome = risultato.getString("nome");
                    String descrizione = risultato.getString("descrizione");
                    int quantitaScorta = risultato.getInt("quantitaScorta");
                    BigDecimal costoInCentesimi = risultato.getBigDecimal("costo");
                    // Il database conserva i centesimi; il modello usa euro.
                    BigDecimal costoInEuro = costoInCentesimi.movePointLeft(2);
                    Integer percentualeScontoLetta =  risultato.getInt("percentualeSconto");
                    // getInt restituisce 0 anche per SQL NULL: wasNull distingue i due casi.
                    Integer percentualeSconto = risultato.wasNull() ? null : percentualeScontoLetta;
                    
                    long idCategoriaProdotto = risultato.getLong("id_categoria");
                    CategoriaComponente categoria = categoriaDAO.trovaPerID(idCategoriaProdotto);

                    if(categoria == null)
                        throw new SQLException("Categoria inesistente per quel prodotto \n");

                    Prodotto prodotto = new Prodotto(nome, codice, descrizione, quantitaScorta, costoInEuro, percentualeSconto, categoria);
                    prodotto.setId(idProdotto);
                    prodotti.add(prodotto);
                } 
            }
        } 
        return prodotti;
    }

}
