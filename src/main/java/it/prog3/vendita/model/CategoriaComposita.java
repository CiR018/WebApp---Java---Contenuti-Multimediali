package it.prog3.vendita.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Categoria che può contenere altre categorie, siano esse foglie o altre
 * categorie composte.
 *
 * <p>La categoria composta possiede un proprio identificativo e un proprio
 * nome. La lista dei figli è mantenuta privata e può essere modificata solo
 * attraverso i metodi pubblici dedicati.</p>
 */
public class CategoriaComposita implements CategoriaComponente {

    private long id;
    private final String nome;
    private final List<CategoriaComponente> componenti;

    /**
     * Crea una categoria composta senza figli e non ancora identificata dal
     * database.
     *
     * @param nome nome della categoria; non può essere {@code null} né vuoto
     *             dopo aver rimosso gli spazi iniziali e finali
     * @throws IllegalArgumentException se il nome è {@code null} o vuoto
     */
    public CategoriaComposita(String nome) {
        
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della categoria non può essere nullo o vuoto.");
        }
        this.nome = nome.trim();
        this.componenti = new ArrayList<>();
    }

    /**
     * Restituisce l'identificativo assegnato a questa categoria.
     *
     * @return identificativo della categoria, oppure {@code 0} se non è ancora
     *         stato assegnato dal database
     */
    @Override
    public long getID() {
        return id;
    }

    /**
     * Restituisce il nome di questa categoria.
     *
     * @return nome della categoria
     */
    @Override
    public String getNome() {
        return nome;
    }

    /**
     * Aggiunge una categoria figlia.
     *
     * <p>Il controllo impedisce di aggiungere {@code this} o un suo antenato,
     * evitando cicli nella gerarchia delle categorie.</p>
     *
     * @param categoria categoria da aggiungere
     * @throws NullPointerException se {@code categoria} è {@code null}
     * @throws IllegalArgumentException se la categoria è questa stessa
     *         istanza, è già presente oppure creerebbe un ciclo
     */
    public void aggiungi(CategoriaComponente categoria) {
        Objects.requireNonNull(categoria, "La categoria figlia non può essere null.");

        if (categoria == this || contiene(categoria, this)) {
            throw new IllegalArgumentException("L'aggiunta creerebbe un ciclo nella gerarchia delle categorie.");
        }
        if (componenti.contains(categoria)) {
            throw new IllegalArgumentException("La categoria è già presente tra i figli.");
        }

        componenti.add(categoria);
    }

    /**
     * Rimuove una categoria figlia.
     *
     * @param categoria categoria da rimuovere
     * @throws NullPointerException se {@code categoria} è {@code null}
     */
    public void rimuovi(CategoriaComponente categoria) {
        Objects.requireNonNull(categoria, "La categoria da rimuovere non può essere null.");
        componenti.remove(categoria);
    }

    /**
     * Restituisce gli identificativi del sottoalbero di questa categoria,
     * includendo prima il suo ID e poi quelli di tutti i discendenti.
     *
     * @return lista immutabile degli identificativi del sottoalbero
     * @throws IllegalStateException se questa categoria o un suo discendente
     *         non ha ancora ricevuto un ID positivo dal database
     */
    @Override
    public List<Long> getIdSottoalbero() {
        if (id <= 0) {
            throw new IllegalStateException("La categoria deve avere un ID assegnato dal database.");
        }

        List<Long> identificativi = new ArrayList<>();
        identificativi.add(id);
        for (CategoriaComponente componente : componenti) {
            identificativi.addAll(componente.getIdSottoalbero());
        }
        return List.copyOf(identificativi);
    }

    /**
     * Assegna l'ID generato dal database.
     *
     * @param id identificativo positivo generato dal database
     * @throws IllegalArgumentException se {@code id} non è positivo
     * @throws IllegalStateException se questa categoria ha già ricevuto un ID
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

    /**
     * Verifica ricorsivamente se il sottoalbero di una categoria contiene una
     * specifica istanza.
     *
     * @param radice categoria da cui iniziare la ricerca
     * @param cercata istanza da cercare
     * @return {@code true} se l'istanza è presente nel sottoalbero
     */
    private boolean contiene(CategoriaComponente radice, CategoriaComponente cercata) {
        if (radice == cercata) {
            return true;
        }
        if (radice instanceof CategoriaComposita composta) {
            for (CategoriaComponente componente : composta.componenti) {
                if (contiene(componente, cercata)) {
                    return true;
                }
            }
        }
        return false;
    }
}
