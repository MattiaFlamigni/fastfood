-- M2: visualizzare totale scontrini
SELECT O.ID, O.data, O.ora, SUM(D.quantita * P.prezzovendita) AS totale_prodotto, 
       (SELECT SUM(D.quantita * P.prezzovendita) FROM DETTAGLIO_ORDINI D JOIN PRODOTTI P ON D.codice_prodotto = P.codice WHERE D.ID_ordine = O.ID) AS totale_scontrino
FROM ORDINE O
JOIN DETTAGLIO_ORDINI D ON O.ID = D.ID_ordine
JOIN PRODOTTI P ON D.codice_prodotto = P.codice
GROUP BY O.ID, O.data, O.ora;

-- M3: Visualizzare magazzino
SELECT nome_commerciale as prodotto, quantita
FROM ingredienti;

-- M4: Visualizzare vendite giornaliere
SELECT O.data, P.descrizione, P.prezzovendita, P.prezzounitario, SUM(D.quantita) AS quantità_totale, SUM(D.quantita * P.prezzovendita) AS totale_vendite
FROM ORDINE O
JOIN DETTAGLIO_ORDINI D ON O.ID = D.ID_ordine
JOIN PRODOTTI P ON D.codice_prodotto = P.codice
WHERE O.data = CURDATE()  -- Filtra gli ordini per la data odierna
GROUP BY O.data, P.descrizione,P.prezzovendita, P.prezzounitario;

-- alternativa (non funziona benissimo)
SELECT O.data, D.codice_prodotto, SUM(D.quantita) AS quantità_totale, SUM(D.quantita * P.prezzovendita) AS totale_vendite
FROM ORDINE O
JOIN (SELECT DISTINCT ID_ordine, codice_prodotto, quantita FROM DETTAGLIO_ORDINI) D ON O.ID = D.ID_ordine
JOIN PRODOTTI P ON D.codice_prodotto = P.codice
WHERE O.data = CURDATE()  -- Filtra gli ordini per la data odierna
GROUP BY O.data, D.codice_prodotto WITH ROLLUP;

-- M5: assegnare slot ai dipendenti
INSERT INTO lavoro (CF_ADDETTO, ora_inizio, ora_fine)
SELECT "???", so.ora_inizio, so.ora_fine
FROM SLOT_ORARIO so
WHERE so.descrizione = 'pranzo';

-- visualizzazione slot assegnati
SELECT A.cognome, S.descrizione
FROM addetto A, lavoro L, slot_orario S
where A.CF = L.CF_addetto and L.ora_inizio=S.ora_inizio and L.ora_fine = S.ora_fine;


-- M6:Visualizzare scontrino medio
SELECT avg(subquery.totale) as scontrino_medio	
	FROM (
	SELECT O.ID, SUM(P.prezzovendita * D.quantita) AS totale
	FROM prodotti P, ordine O, dettaglio_ordini D
	where O.ID=D.ID_ORDINE and P.codice=D.codice_prodotto
    GROUP BY O.ID) AS subquery;

-- M8/M9: fatturato lordo/netto mensile
SELECT SUM(P.prezzovendita * D.quantita) as TotIncassoLordo, SUM(P.prezzounitario*D.quantita) as MateriePrime, SUM(P.prezzovendita * D.quantita)-SUM(P.prezzounitario*D.quantita) as TotNetto
FROM prodotti P, ordine O, dettaglio_ordini D
WHERE D.ID_ordine = O.ID and D.codice_prodotto = P.codice;

-- M11 Visualizzare top10 prodoti
SELECT P.descrizione, SUM(D.quantita) as totale_vendite
FROM dettaglio_ordini D, prodotti P
where P.codice = D.codice_prodotto
group by codice_prodotto
order by totale_vendite desc
LIMIT 10;


-- A5
SELECT SUBSTRING(CAST(SUM(ora_fine-ora_inizio) AS CHAR),1,2) AS TOTALE_ORE
FROM lavoro
WHERE CF_addetto="FLMMTT"
GROUP BY CF_addetto;


select * from addetto;


-- C1
SELECT OFFERTA.codice, OFFERTA.percentuale
FROM OFFERTA
JOIN POSSEDIMENTO_OFFERTA ON OFFERTA.codice = POSSEDIMENTO_OFFERTA.codice_offerta
JOIN CLIENTE ON CLIENTE.ID = 'ID_CLIENTE';








