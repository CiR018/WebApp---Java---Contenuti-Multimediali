--Dati iniziali precaricati--

PRAGMA foreign_keys = ON;

BEGIN;

-- Utenti demo: le password sono segnaposto per testare il database,
-- non consentono il login. Per provare l'accesso, sostituiscile con hash
-- generati dalla classe PasswordHasher del progetto.
INSERT INTO Utente (nome, cognome, ruolo, username, passwordHash) VALUES
('Amministratore', 'Demo', 'AMMINISTRATORE', 'admin.demo',
 'HASH_DEMO_NON_AUTENTICABILE'),
('Marco', 'Rossi', 'CLIENTE', 'marco.rossi',
 'HASH_DEMO_NON_AUTENTICABILE'),
('Giulia', 'Bianchi', 'CLIENTE', 'giulia.bianchi',
 'HASH_DEMO_NON_AUTENTICABILE');

-- Categorie: alcune sono sottocategorie di altre.
INSERT INTO Categoria (nome, id_padre) VALUES
('Film', NULL),
('Musica', NULL),
('Videogiochi', NULL),
('Libri digitali', NULL);

INSERT INTO Categoria (nome, id_padre)
SELECT 'Film digitali', id
FROM Categoria
WHERE nome = 'Film' AND id_padre IS NULL;

INSERT INTO Categoria (nome, id_padre)
SELECT 'Album digitali', id
FROM Categoria
WHERE nome = 'Musica' AND id_padre IS NULL;

INSERT INTO Categoria (nome, id_padre)
SELECT 'Giochi PC', id
FROM Categoria
WHERE nome = 'Videogiochi' AND id_padre IS NULL;

-- Prodotti. costo e prezzoAcquisto sono in centesimi.
INSERT INTO Prodotto
    (codice, nome, descrizione, quantitaScorta, costo,
     percentualeSconto, id_categoria)
VALUES
('FILM-DIG-001', 'Nebbia su Orione',
 'Film di fantascienza in formato digitale.', 120, 1299, 15,
 (SELECT id FROM Categoria WHERE nome = 'Film digitali')),

('FILM-BD-001', 'Oltre il Faro',
 'Film drammatico in edizione Blu-ray.', 35, 2499, NULL,
 (SELECT id FROM Categoria WHERE nome = 'Film')),

('MUS-ALB-001', 'Rotte di Carta',
 'Album musicale in formato digitale.', 200, 999, NULL,
 (SELECT id FROM Categoria WHERE nome = 'Album digitali')),

('GAME-PC-001', 'Cronache di Vetro',
 'Videogioco per PC in formato digitale.', 80, 3999, 10,
 (SELECT id FROM Categoria WHERE nome = 'Giochi PC')),

('EBOOK-001', 'Atlante delle Maree',
 'Romanzo disponibile come e-book.', 150, 799, NULL,
 (SELECT id FROM Categoria WHERE nome = 'Libri digitali'));

-- Ordine completato di Marco: €24,99 + €9,99 = €34,98.
INSERT INTO Ordine
    (dataOrdine, totale, stato, metodoPagamento, id_utente)
VALUES
('2026-10-12 16:40:00', 3498, 'COMPLETATO', 'CARTA_CREDITO',
 (SELECT id FROM Utente WHERE username = 'marco.rossi'));

-- Ordine pagato di Giulia: 2 film a €11,04 ciascuno + e-book a €7,99 = €30,07.
-- Il film costa €12,99 meno il 15%: prezzo arrotondato ai centesimi €11,04.
INSERT INTO Ordine
    (dataOrdine, totale, stato, metodoPagamento, id_utente)
VALUES
('2026-10-19 11:25:00', 3007, 'PAGATO', 'BANCOMAT',
 (SELECT id FROM Utente WHERE username = 'giulia.bianchi'));

INSERT INTO RigaOrdine
    (quantita, prezzoAcquisto, id_ordine, id_prodotto)
VALUES
(1, 2499,
 (SELECT id FROM Ordine
  WHERE id_utente = (SELECT id FROM Utente WHERE username = 'marco.rossi')
    AND dataOrdine = '2026-10-12 16:40:00'),
 (SELECT id FROM Prodotto WHERE codice = 'FILM-BD-001')),

(1, 999,
 (SELECT id FROM Ordine
  WHERE id_utente = (SELECT id FROM Utente WHERE username = 'marco.rossi')
    AND dataOrdine = '2026-10-12 16:40:00'),
 (SELECT id FROM Prodotto WHERE codice = 'MUS-ALB-001')),

(2, 1104,
 (SELECT id FROM Ordine
  WHERE id_utente = (SELECT id FROM Utente WHERE username = 'giulia.bianchi')
    AND dataOrdine = '2026-10-19 11:25:00'),
 (SELECT id FROM Prodotto WHERE codice = 'FILM-DIG-001')),

(1, 799,
 (SELECT id FROM Ordine
  WHERE id_utente = (SELECT id FROM Utente WHERE username = 'giulia.bianchi')
    AND dataOrdine = '2026-10-19 11:25:00'),
 (SELECT id FROM Prodotto WHERE codice = 'EBOOK-001'));


COMMIT;

--crea db
--ciro@Host-001 ~ % sqlite3 "/Users/ciro/Documents/Università/-3 anno/programmazione III/Prg3_progetto/PROG III - Sviluppi/Progettazione_EER_DB/SQL/vendita_verifica.db"

--Inserisci script tabelle e dati
--SQLite-3.54 vendita_verifica.db-> .read "/Users/ciro/Documents/Università/-3 anno/programmazione III/Prg3_progetto/PROG III - Sviluppi/Progettazione_EER_DB/SQL/01_schema.sql"
--SQLite-3.54 vendita_verifica.db-> .read "/Users/ciro/Documents/Università/-3 anno/programmazione III/Prg3_progetto/PROG III - Sviluppi/Progettazione_EER_DB/SQL/02_dati_iniziali.sql"


--Rimuovi db
--SQLite-3.54 vendita_test.db-> rm "/Users/ciro/Documents/Università/-3 anno/programmazione III/Prg3_progetto/PROG III - Sviluppi/Progettazione_EER_DB/SQL/vendita_test.db"