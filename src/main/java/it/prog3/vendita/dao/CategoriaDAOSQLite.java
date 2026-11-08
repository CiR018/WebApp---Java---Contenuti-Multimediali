package it.prog3.vendita.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.prog3.vendita.model.CategoriaComponente;
import it.prog3.vendita.model.CategoriaComposita;
import it.prog3.vendita.model.CategoriaFoglia;



public class CategoriaDAOSQLite implements CategoriaDAO {

    private final ConnessioneDB connessioneDB;

    public CategoriaDAOSQLite(ConnessioneDB connessioneDB) {
        
        if(connessioneDB == null) 
            throw new IllegalArgumentException("ConnessioneDB non può essere null.");

       this.connessioneDB = connessioneDB;
    }

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

    @Override 
    public List<CategoriaComponente> trovaTutte() {
        
        return List.of();
    }

    @Override 
    public void salva(CategoriaComponente categoria) {

    }

    @Override 
    public void aggiorna(CategoriaComponente categoria) {

    }

    @Override
    public void elimina(long id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'elimina'");
    }


}