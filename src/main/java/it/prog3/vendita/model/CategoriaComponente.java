package it.prog3.vendita.model;

import java.util.List;

/**
 * Elemento comune del pattern Composite per la gerarchia delle categorie.
 * Una componente può essere una categoria foglia oppure una categoria
 * composta da altre componenti.
 */
public interface CategoriaComponente {

    /**
     * Restituisce l'identificativo della categoria.
     *
     * @return ID positivo assegnato dal database, oppure {@code 0} se non
     *         ancora assegnato
     */
    long getID();

    /**
     * Restituisce il nome della categoria.
     *
     * @return nome non nullo e non vuoto della categoria
     */
    String getNome();

    /**
     * Restituisce gli identificativi della categoria e di tutti i suoi
     * discendenti. Per una foglia la lista contiene soltanto il suo ID.
     *
     * @return lista immutabile degli ID del sottoalbero
     * @throws IllegalStateException se una categoria del sottoalbero non ha
     *         ancora ricevuto un ID positivo dal database
     */
    List<Long> getIdSottoalbero();

    /**
     * Assegna l'ID generato dal database. Può essere invocato una sola volta
     * per istanza.
     *
     * @param id identificativo positivo generato dal database
     * @throws IllegalArgumentException se l'ID non è positivo
     * @throws IllegalStateException se l'ID è già stato assegnato
     */
    void setId(long id);
}
