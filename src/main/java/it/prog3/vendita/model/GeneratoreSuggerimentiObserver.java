package it.prog3.vendita.model;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.prog3.vendita.dao.ProdottoDAO;

/**
 * Observer del carrello che calcola suggerimenti usando i prodotti appartenenti
 * alle stesse categorie dei prodotti già presenti nel carrello.
 *
 * <p>I prodotti già nel carrello e i suggerimenti duplicati vengono esclusi.
 * La classe usa {@link ProdottoDAO} per leggere il catalogo e non modifica mai
 * il carrello osservato.</p>
 */
public class GeneratoreSuggerimentiObserver implements CarrelloObserver {

    private List<Prodotto> suggerimenti = List.of();
    private final ProdottoDAO prodottoDAO;

    /**
     * Crea il generatore di suggerimenti usando il DAO dei prodotti.
     *
     * @param prodottoDAO DAO utilizzato per cercare i prodotti da suggerire
     * @throws IllegalArgumentException se {@code prodottoDAO} è {@code null}
     */
    public GeneratoreSuggerimentiObserver(ProdottoDAO prodottoDAO) {
        if(prodottoDAO != null)
            this.prodottoDAO = prodottoDAO;
        else 
            throw new IllegalArgumentException("Prodotto DAO non può essere nullo \n");

    }

    /**
     * Riceve una notifica e ricalcola i suggerimenti a partire dallo stato
     * corrente del carrello. Ogni categoria distinta viene consultata una sola
     * volta; i prodotti già presenti nel carrello e i risultati duplicati sono
     * scartati. I suggerimenti precedenti vengono sostituiti solo dopo che tutte
     * le ricerche sono riuscite.
     *
     * @param carrello carrello che ha inviato la notifica
     * @throws IllegalArgumentException se {@code carrello} è {@code null}
     * @throws IllegalStateException se il DAO non riesce a leggere i prodotti;
     *         la causa è l'eccezione SQL originale
     */
    @Override
    public void aggiorna(Carrello carrello) {
       
       if(carrello == null)
        throw new IllegalArgumentException("Il carrello non può essere null! " + carrello + "\n");

        Set<Long> idProdottiNelCarrello = new LinkedHashSet<>();
        Set<Long> idCategorie = new LinkedHashSet<>();

        for(RigaCarrello riga : carrello.getRighe()) {
            Prodotto prodotto = riga.getProdotto();
            idProdottiNelCarrello.add(prodotto.getId());
            idCategorie.add(prodotto.getCategoria().getID());
        }
        Map<Long, Prodotto> nuoviSuggerimenti = new LinkedHashMap<>();

        try {
            for(Long idCategoria : idCategorie) {
               List<Prodotto> prodottiCategoria = prodottoDAO.trovaPerCategoria(idCategoria);

                for (Prodotto candidato : prodottiCategoria) {
                    long idProdotto = candidato.getId();

                    if (!idProdottiNelCarrello.contains(idProdotto)) {
                        nuoviSuggerimenti.putIfAbsent(idProdotto, candidato);
                    }
                }
            }
        }
        catch (SQLException eccezione) {
        throw new IllegalStateException(
                "Impossibile aggiornare i suggerimenti del carrello.",
                eccezione
        );
    }

    suggerimenti = List.copyOf(nuoviSuggerimenti.values());
    }

    /**
     * Restituisce i suggerimenti calcolati all'ultima notifica. La lista
     * restituita è una copia non modificabile e non consente al chiamante di
     * alterare la struttura interna dell'observer.
     *
     * @return copia non modificabile dei prodotti suggeriti; inizialmente è
     *         vuota
     */
    public List<Prodotto> getSuggerimenti() {
        return List.copyOf(suggerimenti);
    }

}
