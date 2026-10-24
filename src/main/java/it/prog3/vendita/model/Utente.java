package it.prog3.vendita.model;

/**
 * La classe utente permette di rappresentare un utente oppure un amministratore in base al ruolo che ricopre
 * Utente
 */
public class Utente {

    private long id;
    private String nome;
    private String cognome;
    private String username;
    private String passwordHash;
    private Ruolo ruolo;


    /**
     * Il costruttore ha i seguenti parametri (id non viene inserito perche è assegnato dal Database)
     * @param nome rappresenta il nome dell'utente
     * @param cognome rappresenta il cognome dell'utente
     * @param username rappresenta lo username dell'utente
     * @param passwordHash e la password crittografata
     * @param ruolo e il ruolo esercitato Cliente o Amministratore
     */

    public Utente(String nome, String cognome, String username, String passwordHash, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
    }

    /** Metodo per restituire l'id
     * @return ritorna l'id
     */
    public long getId() {
        return id;
    }
    /** Metodo per restituire il nome
     * @return ritorna il nome dell'utente
     */
    public String getNome() {
        return nome;
    }
    /** Metodo per restituire il cognome
     * @return ritorna il cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /** Metodo per restituire il ruolo
     * @return ritorna il ruolo utente o amministratore
     */
    public Ruolo getRuolo() {
        return ruolo;
    }
    /** Metodo per restituire lo username
     * @return ritorna lo username
     */
    public String getUsername() {
        return username;
    }

    /** Metodo per restituire il cognome
     * @param cognome cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    /**
     * Il metodo set id viene utilizzato per assegnare un singolo ID da parte del database 
     *Una volta impostato dal database il metodo setId non sarà più utilizzabile<br>
     * Viene controllato se l'id interno di utente sia stato modificato se si lanciamo un'eccezione<br>
     * Se l'id passato al metodo e negativo o zero lanciamo un'errore.<br>
     * In tutti gli altri casi assegniamo l'id per la prima volta.<br>
     * nota: il metodo non deve permettere altri inserimenti dopo la prima modifica da parte del database
     * @param id identificativo del record nel DB
     *@throws IllegalArgumentException per gestire un id non positivo.
     *@throws IllegalStateException per gestire un valore non consentito per id.
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe utente è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe utente deve essere positivo !");
        this.id = id;
    }
    /** Permette l'inserimento del nome
     * @param nome e il nome da settare all'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    /** Permette di inserire la password
     * @param passwordHash la password da assegnare
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    /** permette di memorizzare il ruolo
     * @param ruolo rappresenta il ruolo, che può essere CLIENTE o AMMINISTRATORE
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
    /** Permette di inserire lo username
     * @param username Setta lo username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    

}
