--Creazione Tabelle e Vincoli

CREATE TABLE Utente (
    id INTEGER PRIMARY KEY, 
    nome TEXT NOT NULL,
    cognome TEXT NOT NULL,
    ruolo TEXT NOT NULL CHECK (ruolo IN ('CLIENTE','AMMINISTRATORE')),
    username text NOT NULL UNIQUE, 
    passwordHash TEXT NOT NULL
);

CREATE TABLE Ordine (
    id INTEGER PRIMARY KEY, 
    dataOrdine TEXT NOT NULL, --2026-10-20 14:30:45--
    totale INTEGER NOT NULL CHECK(totale > 0),
    stato TEXT NOT NULL CHECK(stato IN ('IN_ATTESA', 'PAGATO', 'COMPLETATO', 'ANNULLATO')), 
    metodoPagamento TEXT NOT NULL CHECK(metodoPagamento IN ('CONTANTI', 'CARTA_CREDITO', 'BANCOMAT')),
    id_utente INTEGER NOT NULL,
    FOREIGN KEY(id_utente) REFERENCES Utente(id)
);

CREATE TABLE RigaOrdine (
    id INTEGER PRIMARY KEY, 
    quantita INTEGER NOT NULL CHECK(quantita > 0),
    prezzoAcquisto INTEGER NOT NULL CHECK(prezzoAcquisto > 0),
    id_ordine INTEGER NOT NULL,
    id_prodotto INTEGER NOT NULL,
    FOREIGN KEY(id_ordine) REFERENCES Ordine(id),
    FOREIGN KEY(id_prodotto) REFERENCES Prodotto(id)
);

CREATE TABLE Prodotto (
    id INTEGER PRIMARY KEY, 
    codice TEXT NOT NULL UNIQUE,
    nome TEXT NOT NULL,
    descrizione TEXT NOT NULL, 
    quantitaScorta INTEGER NOT NULL CHECK(quantitaScorta >= 0), 
    costo INTEGER NOT NULL CHECK(costo > 0),
    percentualeSconto INTEGER CHECK(percentualeSconto BETWEEN 1 AND 99),
    id_categoria INTEGER NOT NULL,
    FOREIGN KEY(id_categoria) REFERENCES Categoria(id)
);

CREATE TABLE Categoria (
    id INTEGER PRIMARY KEY, 
    nome TEXT NOT NULL,
    id_padre INTEGER,
    FOREIGN KEY(id_padre) REFERENCES Categoria(id)
);