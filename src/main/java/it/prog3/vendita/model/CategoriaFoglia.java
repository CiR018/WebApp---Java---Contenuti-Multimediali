package it.prog3.vendita.model;

import java.util.List;

/**
 * Categoria terminale del pattern Composite: rappresenta una categoria che
 * non contiene altre categorie.
 */
public class CategoriaFoglia implements CategoriaComponente {

    private long id;
    private final String nome;

    /**
     * Crea una categoria foglia priva di ID, che sarà assegnato dal database.
     *
     * @param nome nome della categoria
     * @throws IllegalArgumentException se il nome è {@code null} o vuoto dopo
     *         aver rimosso gli spazi iniziali e finali
     */
    public CategoriaFoglia(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della categoria non può essere nullo o vuoto.");
        }
        this.nome = nome.trim();
    }

    /**
     * Restituisce l'identificativo della categoria.
     *
     * @return ID assegnato dal database, oppure {@code 0} se non ancora assegnato
     */
    @Override
    public long getID() {
        return id;
    }

    /**
     * Restituisce il nome della categoria.
     *
     * @return nome della categoria
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce gli ID del sottoalbero rappresentato da questa foglia.
     * Poiché non ha discendenti, la lista contiene soltanto il suo ID.
     *
     * @return lista immutabile contenente l'ID della categoria
     * @throws IllegalStateException se la categoria non ha ancora ricevuto un
     *         ID positivo dal database
     */
    @Override
    public List<Long> getIdSottoalbero() {
        if (id <= 0) {
            throw new IllegalStateException("La categoria deve avere un ID assegnato dal database.");
        }
        return List.of(id);
    }

    /**
     * Assegna l'ID generato dal database. L'ID può essere impostato una sola
     * volta e deve essere positivo.
     *
     * @param id identificativo positivo generato dal database
     * @throws IllegalArgumentException se {@code id} non è positivo
     * @throws IllegalStateException se a questa categoria è già stato assegnato
     *         un ID
     */
    @Override
    public void setId(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID della categoria deve essere positivo.");
        }
        if (this.id != 0) {
            throw new IllegalStateException("L'ID della categoria è già stato assegnato.");
        }
        this.id = id;
    }
}
