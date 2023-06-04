-- Tabella ADDDETTO
CREATE TABLE ADDETTO (
    CF VARCHAR(10) NOT NULL PRIMARY KEY,
    Nome VARCHAR(50),
    Cognome VARCHAR(50)
);

-- Tabella CONTRATTO
CREATE TABLE CONTRATTO (
    ID INT auto_increment NOT NULL KEY,
    stipendio DECIMAL(10, 2),
    data_fine DATE,
    ore_previste_settimanali INT,
    data_inizio DATE,
    CF_addetto VARCHAR(10),
    FOREIGN KEY (CF_addetto) REFERENCES ADDETTO(CF)
);

-- Tabella SLOT_ORARIO
CREATE TABLE SLOT_ORARIO (
    ora_inizio TIME,
    ora_fine TIME,
    PRIMARY KEY (ora_inizio, ora_fine)
);

-- Tabella LAVORO
CREATE TABLE LAVORO (
    CF_addetto VARCHAR(10),
    ora_inizio TIME,
    ora_fine TIME,
    PRIMARY KEY (CF_addetto, ora_inizio, ora_fine),
    FOREIGN KEY (CF_addetto) REFERENCES ADDETTO(CF),
    FOREIGN KEY (ora_inizio, ora_fine) REFERENCES SLOT_ORARIO(ora_inizio, ora_fine)
);

-- Tabella CLIENTE
CREATE TABLE CLIENTE (
    ID INT AUTO_INCREMENT PRIMARY KEY
);

-- Tabella APPLICAZIONE_CONSEGNA
CREATE TABLE APPLICAZIONE_CONSEGNA (
    nomeApp VARCHAR(100) PRIMARY KEY
);

-- Tabella FORNITORI
CREATE TABLE FORNITORI (
    Piva VARCHAR(20) PRIMARY KEY,
    azienda VARCHAR(100),
    cellulare VARCHAR(20),
    indirizzo VARCHAR(100),
    descrizione VARCHAR(100)
);

-- Tabella PRODOTTI
CREATE TABLE PRODOTTI (
    codice INT PRIMARY KEY,
    quantita INT,
    descrizione VARCHAR(100),
    prezzovendita DECIMAL(10, 2),
    prezzounitario DECIMAL(10, 2)
);

-- Tabella OFFERTA
CREATE TABLE OFFERTA (
    codice INT PRIMARY KEY,
    descrizione VARCHAR(100)
);

-- Tabella TAVOLO
CREATE TABLE TAVOLO (
    numero INT PRIMARY KEY,
    nposti INT
);

-- Tabella BUONOPASTO
CREATE TABLE BUONOPASTO (
    ID INT PRIMARY KEY,
    Descrizione VARCHAR(100)
);

-- Tabella POSSEDIMENTO_OFFERTA
CREATE TABLE POSSEDIMENTO_OFFERTA (
    ID_cliente INT,
    codice_offerta INT,
    PRIMARY KEY (ID_cliente, codice_offerta),
    FOREIGN KEY (ID_cliente) REFERENCES CLIENTE(ID),
    FOREIGN KEY (codice_offerta) REFERENCES OFFERTA(codice)
);

-- Tabella PRENOTAZIONE_TAVOLO
CREATE TABLE PRENOTAZIONE_TAVOLO (
    nominativo VARCHAR(100),
    data DATE,
    n_persone INT,
    ora TIME,
    ID_cliente INT,
    numero INT,
    PRIMARY KEY (nominativo, data, ora),
    FOREIGN KEY (ID_cliente) REFERENCES CLIENTE(ID),
    FOREIGN KEY (numero) REFERENCES TAVOLO(numero)
);

-- Tabella CONTENUTO_BUONI
CREATE TABLE CONTENUTO_BUONI (
    ID_buonopasto INT,
    codice_prodotto INT,
    PRIMARY KEY (ID_buonopasto, codice_prodotto),
    FOREIGN KEY (ID_buonopasto) REFERENCES BUONOPASTO(ID),
    FOREIGN KEY (codice_prodotto) REFERENCES PRODOTTI(codice)
);

-- Tabella ASSEGNAMENTO_BUONI
CREATE TABLE ASSEGNAMENTO_BUONI (
    ora_inizio TIME,
    ora_fine TIME,
    ID_buonopasto INT,
    PRIMARY KEY (ora_inizio, ora_fine, ID_buonopasto),
    FOREIGN KEY (ora_inizio, ora_fine) REFERENCES SLOT_ORARIO(ora_inizio, ora_fine),
    FOREIGN KEY (ID_buonopasto) REFERENCES BUONOPASTO(ID)
);

-- Tabella INGREDIENTI
CREATE TABLE INGREDIENTI (
    ID INT PRIMARY KEY,
    prezzoUnitario DECIMAL(10, 2),
    nome_commerciale VARCHAR(100)
);

-- Tabella INGREDIENTI_PRODOTTI
CREATE TABLE INGREDIENTI_PRODOTTI (
    ID_ingrediente INT,
    codice_prodotto INT,
    PRIMARY KEY (ID_ingrediente, codice_prodotto),
    FOREIGN KEY (ID_ingrediente) REFERENCES INGREDIENTI(ID),
    FOREIGN KEY (codice_prodotto) REFERENCES PRODOTTI(codice)
);

-- Tabella ORDINE
CREATE TABLE ORDINE (
    ID INT PRIMARY KEY,
    data DATE,
    ora TIME,
    ID_cliente INT,
    codice_offerta INT,
    FOREIGN KEY (ID_cliente) REFERENCES CLIENTE(ID),
    FOREIGN KEY (codice_offerta) REFERENCES OFFERTA(codice)
);

-- Tabella SCARTI_GIORNALIERI
CREATE TABLE SCARTI_GIORNALIERI (
    data DATE,
    prodotto VARCHAR(100),
    quantita INT,
    PRIMARY KEY (data, prodotto)
);

-- Tabella PRODOTTI_SCARTATI
CREATE TABLE PRODOTTI_SCARTATI (
    codice_prodotto INT,
    data DATE,
    prodotto VARCHAR(100),
    PRIMARY KEY (codice_prodotto, data, prodotto),
    FOREIGN KEY (codice_prodotto) REFERENCES PRODOTTI(codice),
    FOREIGN KEY (data, prodotto) REFERENCES SCARTI_GIORNALIERI(data, prodotto)
);

-- Tabella RIFORNIMENTO
CREATE TABLE RIFORNIMENTO (
    Piva_fornitore VARCHAR(20),
    ID_ingrediente INT,
    PRIMARY KEY (Piva_fornitore, ID_ingrediente),
    FOREIGN KEY (Piva_fornitore) REFERENCES FORNITORI(Piva),
    FOREIGN KEY (ID_ingrediente) REFERENCES INGREDIENTI(ID)
);

-- Tabella DETTAGLIO_ORDINI
CREATE TABLE DETTAGLIO_ORDINI (
    quantita INT,
    ID_ordine INT,
    codice_prodotto INT,
    PRIMARY KEY (quantita, ID_ordine, codice_prodotto),
    FOREIGN KEY (ID_ordine) REFERENCES ORDINE(ID),
    FOREIGN KEY (codice_prodotto) REFERENCES PRODOTTI(codice)
);

-- Tabella DETTAGLIO_CONSEGNA
CREATE TABLE DETTAGLIO_CONSEGNA (
    quantita INT,
    ID_ordine INT,
    nomeApp VARCHAR(100),
    PRIMARY KEY (quantita, ID_ordine, nomeApp),
    FOREIGN KEY (ID_ordine) REFERENCES ORDINE(ID),
    FOREIGN KEY (nomeApp) REFERENCES APPLICAZIONE_CONSEGNA(nomeApp)
);

-- Tabella DIRETTORI temporanea per evitare dipendenza circolare
CREATE TABLE DIRETTORI (
    CF VARCHAR(10) PRIMARY KEY,
    Nome VARCHAR(50),
    Cognome VARCHAR(50)
    -- dataRichiesta DATE
    -- FOREIGN KEY (data) REFERENCES RICHIESTE(data)
);

-- Tabella MANAGER
CREATE TABLE MANAGER (
    CF VARCHAR(10) NOT NULL PRIMARY KEY,
    Nome VARCHAR(50),
    Cognome VARCHAR(50),
    CF_direttore VARCHAR(10),
    FOREIGN KEY (CF_direttore) REFERENCES DIRETTORI(CF)
);

-- Tabella RICHIESTE
CREATE TABLE RICHIESTE (
    dataRichiesta DATE NOT NULL PRIMARY KEY,
    datainizio DATE,
    datafine DATE,
    tipo VARCHAR(50),
    CF_addetto VARCHAR(10),
    CF_manager VARCHAR(10),
    FOREIGN KEY (CF_addetto) REFERENCES ADDETTO(CF),
    FOREIGN KEY (CF_manager) REFERENCES MANAGER(CF)
);


-- a causa di una dipendenza circolare, ridefinisco correttamente
ALTER TABLE direttori
ADD COLUMN dataRichiesta DATE;

ALTER TABLE DIRETTORI
ADD CONSTRAINT fk_richieste_data
FOREIGN KEY (dataRichiesta) REFERENCES RICHIESTE(dataRichiesta);






-- Tabella MANAGER
CREATE TABLE MANAGER (
    CF VARCHAR(10) NOT NULL PRIMARY KEY,
    Nome VARCHAR(50),
    Cognome VARCHAR(50),
    CF_direttore VARCHAR(10),
    FOREIGN KEY (CF_direttore) REFERENCES DIRETTORI(CF)
);

-- Tabella ADDETTO_MANAGER
CREATE TABLE ADDETTO_MANAGER (
    CF_addetto VARCHAR(10),
    CF_manager VARCHAR(10),
    PRIMARY KEY (CF_addetto, CF_manager),
    FOREIGN KEY (CF_addetto) REFERENCES ADDETTO(CF),
    FOREIGN KEY (CF_manager) REFERENCES MANAGER(CF)
);

-- Tabella ORDINEAPPROVIGIONAMENTO
CREATE TABLE ORDINEAPPROVIGIONAMENTO (
    ID INT PRIMARY KEY,
    prezzo DECIMAL(10, 2),
    data DATE,
    CF_manager VARCHAR(10),
    Piva_fornitori VARCHAR(20),
    FOREIGN KEY (CF_manager) REFERENCES MANAGER(CF),
    FOREIGN KEY (Piva_fornitori) REFERENCES FORNITORI(Piva)
);

-- Tabella INCREMENTO
CREATE TABLE INCREMENTO (
    ID_ordineapprovvigionamento INT,
    ID_ingrediente INT,
    PRIMARY KEY (ID_ordineapprovvigionamento, ID_ingrediente),
    FOREIGN KEY (ID_ordineapprovvigionamento) REFERENCES ORDINEAPPROVIGIONAMENTO(ID),
    FOREIGN KEY (ID_ingrediente) REFERENCES INGREDIENTI(ID)
);

-- Tabella CONTEGGIO_SCARTI
CREATE TABLE CONTEGGIO_SCARTI (
    CF_manager VARCHAR(10),
    data DATE,
    prodotto VARCHAR(100),
    PRIMARY KEY (CF_manager, data, prodotto),
    FOREIGN KEY (CF_manager) REFERENCES MANAGER(CF),
    FOREIGN KEY (data, prodotto) REFERENCES SCARTI_GIORNALIERI(data, prodotto)
);

-- Tabella SPESEEXTRA
CREATE TABLE SPESEEXTRA (
    ID INT PRIMARY KEY,
    data DATE,
    descrizione VARCHAR(100),
    totale DECIMAL(10, 2),
    CF_direttori VARCHAR(10),
    FOREIGN KEY (CF_direttori) REFERENCES DIRETTORI(CF)
);

-- Tabella STORICO parziale
CREATE TABLE STORICO (
    ID INT PRIMARY KEY,
    ID_carta INT,
    ID_cliente INT,
    -- FOREIGN KEY (ID_carta) REFERENCES FIDELTY(numero),
    FOREIGN KEY (ID_cliente) REFERENCES CLIENTE(ID)
);

-- Tabella FIDELTY parziale
CREATE TABLE FIDELTY (
    numero INT PRIMARY KEY,
    n_timbri INT,
    scadenza DATE,
    menuomaggio VARCHAR(100),
    ID_cliente INT,
    ID_storico INT,
    FOREIGN KEY (ID_cliente) REFERENCES CLIENTE(ID)
    -- FOREIGN KEY (ID_storico) REFERENCES STORICO(ID)
);

ALTER TABLE FIDELTY
ADD CONSTRAINT fk_ID_storico
FOREIGN KEY (ID_storico) REFERENCES STORICO(ID);


-- DA MODIFICARE NELLA RELAZIONE

ALTER TABLE ingredienti
ADD COLUMN quantita INT;

ALTER TABLE ingredienti_prodotti
ADD COLUMN quantita_utilizzata DECIMAL(10,2);

ALTER TABLE slot_orario
ADD COLUMN descrizione varchar(10);





