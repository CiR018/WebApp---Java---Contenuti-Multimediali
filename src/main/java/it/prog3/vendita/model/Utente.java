package it.prog3.vendita.model;

/**
 * Rappresenta un account del sistema, identificato da nome, cognome e username,
 * con un ruolo e le credenziali necessarie per l'autenticazione.
 * <p>
 * {@code passwordHash} contiene l'hash della password, non la password in
 * chiaro. L'identificativo viene assegnato dal database dopo il salvataggio.
 */
public class Utente {

    private long id;
    private String nome;
    private String cognome;
    private String username;
    private String passwordHash;
    private Ruolo ruolo;


    /**
     * Crea un account con i dati anagrafici, le credenziali e il ruolo indicati.
     * L'identificativo non è richiesto perché viene assegnato dal database.
     * <p>
     * Per la registrazione pubblica, il livello applicativo deve fornire
     * {@link Ruolo#CLIENTE}; il ruolo non deve essere scelto tramite un valore
     * inviato dal browser. Il parametro consente anche di ricostruire account
     * amministrativi già presenti nel database.
     *
     * @param nome nome dell'utente
     * @param cognome cognome dell'utente
     * @param username nome utente univoco usato per l'accesso
     * @param passwordHash hash della password prodotto da {@code PasswordHasher};
     *                     non deve contenere la password in chiaro
     * @param ruolo ruolo assegnato all'account
     * @throws IllegalArgumentException se uno dei parametri obbligatori è diverso da quello aspettato
     * @throws NullPointerException se il ruolo ha valore null
     */

    public Utente(String nome, String cognome, String username, String passwordHash, Ruolo ruolo) {

        if(nome == null)
            throw new IllegalArgumentException("Il nome non può essere vuoto. \n");
        else 
            this.nome = nome;

        if(cognome == null)
            throw new IllegalArgumentException("Il cognome non può essere vuoto. \n");
        else 
            this.cognome = cognome;    
        
        if(username == null)
            throw new IllegalArgumentException("Il username non può essere vuoto. \n");
        else 
            this.username = username;
       
        if(passwordHash == null)
            throw new IllegalArgumentException("La password non può essere vuota. \n");
        else 
            this.passwordHash = passwordHash;
        
        if (ruolo == null) 
            throw new IllegalArgumentException("Il ruolo non può essere null.");
        else
            this.ruolo = ruolo;
        
    }

    /**
     * Restituisce l'hash della password memorizzato per l'account.
     * Il valore serve al flusso di autenticazione e non deve essere esposto
     * nelle pagine o nelle risposte inviate al browser.
     *
     * @return hash della password
     */
    public String getPasswordHash() {

        return passwordHash;
    }

    /**
     * Restituisce l'identificativo assegnato dal database, oppure zero se
     * l'account non è ancora stato salvato.
     *
     * @return identificativo dell'account
     */
    public long getId() {
        return id;
    }
    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome dell'utente
     */
    public String getNome() {
        return nome;
    }
    /**
     * Restituisce il cognome dell'utente.
     *
     * @return cognome dell'utente
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce il ruolo assegnato all'account.
     *
     * @return ruolo dell'account
     */
    public Ruolo getRuolo() {
        return ruolo;
    }
    /**
     * Restituisce lo username usato per identificare l'account durante
     * l'accesso.
     *
     * @return username dell'account
     */
    public String getUsername() {
        return username;
    }

    /**
     * Aggiorna il cognome dell'utente.
     *
     * @param cognome nuovo cognome dell'utente
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    /**
     * Assegna all'account l'identificativo positivo generato dal database.
     * L'identificativo può essere assegnato una sola volta.
     *
     * @param id identificativo generato dal database
     * @throws IllegalArgumentException se {@code id} è minore o uguale a zero
     * @throws IllegalStateException se all'account è già stato assegnato un ID
     */
    public void setId(long id) {
        if(this.id != 0)
            throw new IllegalStateException("L'ID della classe utente è stato già assegnato dal Database");
        if (id <= 0) 
            throw new IllegalArgumentException("L'ID della classe utente deve essere positivo !");
        this.id = id;
    }
    /**
     * Aggiorna il nome dell'utente.
     *
     * @param nome nuovo nome dell'utente
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    /**
     * Aggiorna l'hash della password dell'account.
     * Il parametro deve essere un hash prodotto da {@code PasswordHasher},
     * non la password in chiaro.
     *
     * @param passwordHash nuovo hash della password
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    /**
     * Aggiorna il ruolo dell'account.
     * La logica applicativa deve impedire che un utente imposti autonomamente
     * il ruolo tramite dati ricevuti dal browser.
     *
     * @param ruolo nuovo ruolo dell'account
     */
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
    /**
     * Aggiorna lo username dell'account.
     * L'unicità dello username è garantita dal vincolo del database.
     *
     * @param username nuovo username dell'account
     */
    public void setUsername(String username) {
        this.username = username;
    }
    

}
