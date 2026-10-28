package it.prog3.vendita.model;

import java.util.List;

import it.prog3.vendita.dao.ProdottoDAO;

/**
 * Observer del carrello che mantiene i prodotti suggeriti in base ai prodotti
 * presenti nel carrello. Usa {@link ProdottoDAO} per reperire i prodotti dal
 * catalogo.
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
     * Riceve una notifica del carrello e aggiorna i suggerimenti in base al suo
     * contenuto corrente.
     *
     * @param carrello carrello che ha inviato la notifica
     */
    @Override
    public void aggiorna(Carrello carrello) {
       
    }

    /**
     * Restituisce i suggerimenti attualmente memorizzati. La lista restituita
     * è una copia non modificabile e non consente al chiamante di alterare lo
     * stato interno dell'observer.
     *
     * @return copia non modificabile dei prodotti suggeriti; inizialmente è
     *         vuota
     */
    public List<Prodotto> getSuggerimenti() {
        return List.copyOf(suggerimenti);
    }

}
