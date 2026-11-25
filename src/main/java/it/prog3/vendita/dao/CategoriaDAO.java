package it.prog3.vendita.dao;

import it.prog3.vendita.model.CategoriaComponente;
import java.sql.SQLException;
import java.util.List;

/**
 * Definisce le operazioni di persistenza per le categorie e sottocategorie.
 */
public interface CategoriaDAO {

    /**
     * Cerca una categoria tramite il suo identificativo.
     * @param id identificativo della categoria
     * @return categoria trovata, oppure null se non esiste
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    CategoriaComponente trovaPerID(long id) throws SQLException;

    /**
     * Recupera le categorie radice con i rispettivi sottoalberi.
     * @return categorie radice con i rispettivi sottoalberi; lista vuota se
     * non ci sono categorie
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    List<CategoriaComponente> trovaTutte() throws SQLException;

    /**
     * Salva una categoria o sottocategoria e assegna l'ID generato dal database.
     * @param categoria componente di categoria da salvare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void salva(CategoriaComponente categoria) throws SQLException;

    /**
     * Aggiorna una categoria o sottocategoria già salvata.
     * @param categoria componente con i dati aggiornati
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void aggiorna(CategoriaComponente categoria) throws SQLException;

    /**
     * Elimina la categoria con l'identificativo indicato.
     * @param id identificativo della categoria da eliminare
     * @throws SQLException se si verifica un errore durante l'accesso ai dati
     */
    void elimina(long id) throws SQLException;
}
