package it.prog3.vendita.model;

public class RigaCarrello {
    private Prodotto prodotto;
    private int quantita;

    public RigaCarrello(Prodotto prodotto, int quantita) {
        if(prodotto != null)
            this.prodotto = prodotto;
        else 
            throw new IllegalArgumentException("Il prodotto deve essere valorizzato nella Riga del carrello! " + prodotto + "\n");

        if(quantita > 0 ) 
            this.quantita = quantita;
        else 
            throw new IllegalArgumentException("La quantità da memorizzare nella riga del carrello deve essere almeno pari a 1! " + quantita + "\n");      
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
