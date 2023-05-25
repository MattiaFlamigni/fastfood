-- inserimento cliente
INSERT INTO cliente (ID)
VALUES (1);

-- inserimento prodotti
INSERT INTO prodotti(codice, quantita, descrizione, prezzovendita, prezzounitario)
VALUES (002,1000, "Parmigiano",7,4);

-- Inserimento di un nuovo ordine
INSERT INTO ORDINE (ID, data, ora, Id_cliente)
VALUES (1, '2023-05-23', '14:30:00', 1);

-- associazione ordine - prodotti
INSERT INTO DETTAGLIO_ORDINI (ID_ordine, codice_prodotto, quantita)
VALUES (3,2,3);

select * from dettaglio_ordini;


INSERT INTO ingredienti(ID, prezzoUnitario, nome_commerciale, quantita)
VALUES (1,0.1,"Insalata",30),(2,0.1,"Pomodoro",30),(3,0.2,"Cetriolino",60),(4,0.1,"Cipolla",30);



-- calcolo del totale di ogni prodotto    prodotto*quantità
SELECT P.descrizione, SUM(D.quantita * P.prezzovendita) AS totale
FROM DETTAGLIO_ORDINI D
JOIN PRODOTTI P ON D.codice_prodotto = P.codice
WHERE D.ID_ordine = 1
GROUP BY P.DESCRIZIONE;


-- associo a un prodotto gli ingredienti con le quantità 
INSERT INTO INGREDIENTI_PRODOTTI (codice_prodotto, ID_ingrediente, quantita_utilizzata)
VALUES
  (1, 1, 0.1),  
  (1, 2, 0.1), 
  (1, 3, 4), 
  (1, 4, 0.3); 

-- per ogni prodotto visualizzare gli ingredienti  
SELECT P.codice AS codice_prodotto, P.descrizione AS descrizione_prodotto, I.nome_commerciale, PI.quantita_utilizzata
FROM PRODOTTI P
JOIN INGREDIENTI_PRODOTTI PI ON P.codice = PI.codice_prodotto
JOIN INGREDIENTI I ON PI.ID_ingrediente = I.ID
GROUP BY P.codice, P.descrizione, I.nome_commerciale, PI.quantita_utilizzata;


-- inserimento slot
INSERT INTO slot_orario(ora_inizio, ora_fine)
VALUES (12,15), (15, 19), (19, 24);








