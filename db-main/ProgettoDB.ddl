-- *********************************************
-- * SQL MySQL generation                      
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Tue Aug  9 15:55:23 2022 
-- * LUN file: \\192.168.1.142\NetBackup\BasiDiDati\Progetto\ProgettoDB.lun 
-- * Schema: CooperativaSanitaria/L2 
-- ********************************************* 


-- Database Section
-- ________________ 

create database CooperativaSanitaria;
use CooperativaSanitaria;


-- Tables Section
-- _____________ 

create table ASSUMERE_TERAPIA (
     CodiceFiscale char(16) not null,
     CodiceTerapia int not null,
     constraint ID_ASSUMERE_TERAPIA_ID primary key (CodiceTerapia, CodiceFiscale));

create table ATTESTATO_ACQUISITO (
     CodiceFiscale char(16) not null,
     DataAcquisizione date not null,
     Nome char(30) not null,
     constraint ID_ATTESTATO_ACQUISITO_ID primary key (CodiceFiscale, DataAcquisizione));

create table BENI_STRUMENTALI (
     CodiceUnita char(5) not null,
     CodBene int not null,
     DataAcquisto date not null,
     ProssimaManutenzione date not null,
     Automezzo char not null,
     NomeAttrezzo char(20),
     Targa char(10),
     Tipologia char(20),
     ScadenzaAssicurazione date,
     constraint ID_BENI_STRUMENTALI_ID primary key (CodiceUnita, CodBene));

create table CARTELLA_CLINICA (
     CodCartellaClinica int not null,
     CodiceFiscale char(16) not null,
     Anamnesi char(255) not null,
     Diagnosi char(255) not null,
     ProgettoRiabilitativo char(255) not null,
     constraint ID_CARTELLA_CLINICA_ID primary key (CodCartellaClinica),
     constraint FKIDENTIFICARE_ID unique (CodiceFiscale));

create table CONTRATTO_STIPULATO (
     CodiceFiscale char(16) not null,
     DataStipulazione date not null,
     DataFine date not null,
     Nome char(20) not null,
     constraint ID_CONTRATTO_STIPULATO_ID primary key (CodiceFiscale, DataStipulazione));

create table DIPENDENTE (
     CodiceFiscale char(16) not null,
     Nome char(15) not null,
     Cognome char(15) not null,
     Compleanno date not null,
     Residenza char(50) not null,
     Sesso char(1) not null,
     CodiceDipendente int not null,
     TitoloDiStudio char(50) not null,
     IdoneitaAllaMansione char not null,
     Socio char not null,
     CreditiECM int not null,
     constraint SID_DIPENDENTE_ID unique (CodiceDipendente),
     constraint ID_DIPENDENTE_ID primary key (CodiceFiscale));

create table FARMACO (
     CodFarmaco int not null,
     Nome char(20) not null,
     CasaFarmaceutica char(1) not null,
     DataAcquisto date not null,
     DataScadenza date not null,
     Quantita int not null,
     constraint ID_FARMACO_ID primary key (CodFarmaco));

create table FARMACO_TERAPIA (
     CodiceTerapia int not null,
     CodiceConsumazione int not null,
     DataAssunzione date not null,
     Quantita int not null,
     CodiceFiscale char(16) not null,
     CodFarmaco int not null,
     constraint ID_FARMACO_TERAPIA_ID primary key (CodiceTerapia, CodiceConsumazione));

create table OSPITAZIONE (
     CodiceFiscale char(16) not null,
     DataInizio date not null,
     DataFine date,
     CodiceUnita char(5) not null,
     constraint ID_OSPITAZIONE_ID primary key (CodiceFiscale, DataInizio));

create table PAZIENTE (
     CodiceFiscale char(16) not null,
     Nome char(15) not null,
     Cognome char(15) not null,
     Compleanno date not null,
     Residenza char(50) not null,
     Sesso char(1) not null,
     CodicePaziente int not null,
     DocumentazionePrivacy char not null,
     ConsensoInformatoAlTrattamento char not null,
     AccettazioneRegolamento char not null,
     constraint SID_PAZIENTE_ID unique (CodicePaziente),
     constraint ID_PAZIENTE_ID primary key (CodiceFiscale));

create table TERAPIA (
     CodiceTerapia int not null,
     DataInizio date not null,
     constraint ID_TERAPIA_ID primary key (CodiceTerapia));

create table TIPOLOGIA_ATTESTATO (
     Nome char(30) not null,
     CreditiECM int not null,
     constraint ID_TIPOLOGIA_ATTESTATO_ID primary key (Nome));

create table TIPOLOGIA_CONTRATTO (
     Nome char(20) not null,
     OreContrattuali int not null,
     constraint ID_TIPOLOGIA_CONTRATTO_ID primary key (Nome));

create table TURNO (
     CodiceFiscale char(16) not null,
     GiornoSettimana char(10) not null,
     OraInizio date not null,
     OraFine date not null,
     CodiceUnita char(5) not null,
     constraint ID_TURNO_ID primary key (CodiceFiscale, GiornoSettimana, OraInizio));

create table UNITA_OPERATIVA (
     CodiceUnita char(5) not null,
     Tipologia char(20) not null,
     Denominazione char(20) not null,
     Ubicazione char(50) not null,
     PostiLetto int not null,
     NumeroPazienti int not null,
     AutorizzazioneAlFunzionamento char not null,
     Accreditamento char not null,
     constraint ID_UNITA_OPERATIVA_ID primary key (CodiceUnita));


-- Constraints Section
-- ___________________ 

alter table ASSUMERE_TERAPIA add constraint FKASS_TER
     foreign key (CodiceTerapia)
     references TERAPIA (CodiceTerapia);

alter table ASSUMERE_TERAPIA add constraint FKASS_PAZ_FK
     foreign key (CodiceFiscale)
     references PAZIENTE (CodiceFiscale);

alter table ATTESTATO_ACQUISITO add constraint FKCONSEGUIRE
     foreign key (CodiceFiscale)
     references DIPENDENTE (CodiceFiscale);

alter table ATTESTATO_ACQUISITO add constraint FKACQUISIZIONE_FK
     foreign key (Nome)
     references TIPOLOGIA_ATTESTATO (Nome);

alter table BENI_STRUMENTALI add constraint FKPOSSEDERE
     foreign key (CodiceUnita)
     references UNITA_OPERATIVA (CodiceUnita);

alter table CARTELLA_CLINICA add constraint FKIDENTIFICARE_FK
     foreign key (CodiceFiscale)
     references PAZIENTE (CodiceFiscale);

alter table CONTRATTO_STIPULATO add constraint FKSTIPULARE_FK
     foreign key (Nome)
     references TIPOLOGIA_CONTRATTO (Nome);

alter table CONTRATTO_STIPULATO add constraint FKFIRMARE
     foreign key (CodiceFiscale)
     references DIPENDENTE (CodiceFiscale);

-- Not implemented
-- alter table DIPENDENTE add constraint ID_DIPENDENTE_CHK
--     check(exists(select * from TURNO
--                  where TURNO.CodiceFiscale = CodiceFiscale)); 

-- Not implemented
-- alter table DIPENDENTE add constraint ID_DIPENDENTE_CHK
--     check(exists(select * from CONTRATTO_STIPULATO
--                  where CONTRATTO_STIPULATO.CodiceFiscale = CodiceFiscale)); 

alter table FARMACO_TERAPIA add constraint FKSOMMINISTRA_FK
     foreign key (CodiceFiscale)
     references DIPENDENTE (CodiceFiscale);

alter table FARMACO_TERAPIA add constraint FKDEFINIRE
     foreign key (CodiceTerapia)
     references TERAPIA (CodiceTerapia);

alter table FARMACO_TERAPIA add constraint FKCONSUMARE_FK
     foreign key (CodFarmaco)
     references FARMACO (CodFarmaco);

alter table OSPITAZIONE add constraint FKOSPITARE
     foreign key (CodiceFiscale)
     references PAZIENTE (CodiceFiscale);

alter table OSPITAZIONE add constraint FKFORNIRE_FK
     foreign key (CodiceUnita)
     references UNITA_OPERATIVA (CodiceUnita);

-- Not implemented
-- alter table PAZIENTE add constraint ID_PAZIENTE_CHK
--     check(exists(select * from ASSUMERE_TERAPIA
--                  where ASSUMERE_TERAPIA.CodiceFiscale = CodiceFiscale)); 

-- Not implemented
-- alter table PAZIENTE add constraint ID_PAZIENTE_CHK
--     check(exists(select * from CARTELLA_CLINICA
--                  where CARTELLA_CLINICA.CodiceFiscale = CodiceFiscale)); 

-- Not implemented
-- alter table PAZIENTE add constraint ID_PAZIENTE_CHK
--     check(exists(select * from OSPITAZIONE
--                  where OSPITAZIONE.CodiceFiscale = CodiceFiscale)); 

-- Not implemented
-- alter table TERAPIA add constraint ID_TERAPIA_CHK
--     check(exists(select * from ASSUMERE_TERAPIA
--                  where ASSUMERE_TERAPIA.CodiceTerapia = CodiceTerapia)); 

alter table TURNO add constraint FKSVOLGERE_FK
     foreign key (CodiceUnita)
     references UNITA_OPERATIVA (CodiceUnita);

alter table TURNO add constraint FKASSEGNARE
     foreign key (CodiceFiscale)
     references DIPENDENTE (CodiceFiscale);

-- Not implemented
-- alter table UNITA_OPERATIVA add constraint ID_UNITA_OPERATIVA_CHK
--     check(exists(select * from OSPITAZIONE
--                  where OSPITAZIONE.CodiceUnita = CodiceUnita)); 

-- Not implemented
-- alter table UNITA_OPERATIVA add constraint ID_UNITA_OPERATIVA_CHK
--     check(exists(select * from BENI_STRUMENTALI
--                  where BENI_STRUMENTALI.CodiceUnita = CodiceUnita)); 

-- Not implemented
-- alter table UNITA_OPERATIVA add constraint ID_UNITA_OPERATIVA_CHK
--     check(exists(select * from TURNO
--                  where TURNO.CodiceUnita = CodiceUnita)); 


-- Index Section
-- _____________ 

create unique index ID_ASSUMERE_TERAPIA_IND
     on ASSUMERE_TERAPIA (CodiceTerapia, CodiceFiscale);

create index FKASS_PAZ_IND
     on ASSUMERE_TERAPIA (CodiceFiscale);

create unique index ID_ATTESTATO_ACQUISITO_IND
     on ATTESTATO_ACQUISITO (CodiceFiscale, DataAcquisizione);

create index FKACQUISIZIONE_IND
     on ATTESTATO_ACQUISITO (Nome);

create unique index ID_BENI_STRUMENTALI_IND
     on BENI_STRUMENTALI (CodiceUnita, CodBene);

create unique index ID_CARTELLA_CLINICA_IND
     on CARTELLA_CLINICA (CodCartellaClinica);

create unique index FKIDENTIFICARE_IND
     on CARTELLA_CLINICA (CodiceFiscale);

create unique index ID_CONTRATTO_STIPULATO_IND
     on CONTRATTO_STIPULATO (CodiceFiscale, DataStipulazione);

create index FKSTIPULARE_IND
     on CONTRATTO_STIPULATO (Nome);

create unique index SID_DIPENDENTE_IND
     on DIPENDENTE (CodiceDipendente);

create unique index ID_DIPENDENTE_IND
     on DIPENDENTE (CodiceFiscale);

create unique index ID_FARMACO_IND
     on FARMACO (CodFarmaco);

create unique index ID_FARMACO_TERAPIA_IND
     on FARMACO_TERAPIA (CodiceTerapia, CodiceConsumazione);

create index FKSOMMINISTRA_IND
     on FARMACO_TERAPIA (CodiceFiscale);

create index FKCONSUMARE_IND
     on FARMACO_TERAPIA (CodFarmaco);

create unique index ID_OSPITAZIONE_IND
     on OSPITAZIONE (CodiceFiscale, DataInizio);

create index FKFORNIRE_IND
     on OSPITAZIONE (CodiceUnita);

create unique index SID_PAZIENTE_IND
     on PAZIENTE (CodicePaziente);

create unique index ID_PAZIENTE_IND
     on PAZIENTE (CodiceFiscale);

create unique index ID_TERAPIA_IND
     on TERAPIA (CodiceTerapia);

create unique index ID_TIPOLOGIA_ATTESTATO_IND
     on TIPOLOGIA_ATTESTATO (Nome);

create unique index ID_TIPOLOGIA_CONTRATTO_IND
     on TIPOLOGIA_CONTRATTO (Nome);

create unique index ID_TURNO_IND
     on TURNO (CodiceFiscale, GiornoSettimana, OraInizio);

create index FKSVOLGERE_IND
     on TURNO (CodiceUnita);

create unique index ID_UNITA_OPERATIVA_IND
     on UNITA_OPERATIVA (CodiceUnita);

