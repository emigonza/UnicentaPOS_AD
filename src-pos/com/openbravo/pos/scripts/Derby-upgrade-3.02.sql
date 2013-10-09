--    uniCenta oPOS - Touch Friendly Point Of Sale
--    Copyright (C) 2009-2013 uniCenta
--    http://www.unicenta.net
--
--    This file is part of uniCenta oPOS.
--
--    uniCenta oPOS is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.
--
--    uniCenta oPOS is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.
--
--    You should have received a copy of the GNU General Public License
--    along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.

-- Database upgrade script for DERBY
-- v3.02 - v3.50

--
-- CREATE new tables
--
CREATE TABLE csvimport (
  ID VARCHAR(256) NOT NULL,
  ROWNUMBER VARCHAR(256),
  CSVERROR VARCHAR(256),
  REFERENCE VARCHAR(256),
  CODE VARCHAR(256),
  NAME VARCHAR(256),
  PRICEBUY DOUBLE,
  PRICESELL DOUBLE,
  PREVIOUSBUY DOUBLE,
  PREVIOUSSELL DOUBLE,
  PRIMARY KEY (ID)
);

CREATE TABLE PICKUP_NUMBER (
ID INTEGER NOT NULL
);

INSERT INTO PICKUP_NUMBER VALUES(1);

--
-- John L addition remains
--
CREATE TABLE MOORERS (
  VESSELNAME VARCHAR(256),
  SIZE INTEGER,
  DAYS INTEGER,
  POWER SMALLINT DEFAULT 0 NOT NULL
  );

--
-- ALTER existing tables
--
ALTER TABLE CATEGORIES ADD COLUMN TEXTTIP VARCHAR(256) DEFAULT NULL;
ALTER TABLE CATEGORIES ADD COLUMN CATSHOWNAME SMALLINT DEFAULT 1 NOT NULL;

ALTER TABLE CUSTOMERS ADD COLUMN IMAGE BLOB;

ALTER TABLE PLACES ADD COLUMN CUSTOMER VARCHAR(256);
ALTER TABLE PLACES ADD COLUMN WAITER VARCHAR(256);
ALTER TABLE PLACES ADD COLUMN TICKETID VARCHAR(256);
ALTER TABLE PLACES ADD COLUMN TABLEMOVED SMALLINT DEFAULT 0 NOT NULL;

ALTER TABLE PRODUCTS ADD COLUMN ISVPRICE SMALLINT DEFAULT 0 NOT NULL;
ALTER TABLE PRODUCTS ADD COLUMN ISVERPATRIB SMALLINT DEFAULT 0 NOT NULL;
ALTER TABLE PRODUCTS ADD COLUMN TEXTTIP VARCHAR(256) DEFAULT NULL;
ALTER TABLE PRODUCTS ADD COLUMN WARRANTY SMALLINT DEFAULT 0 NOT NULL;

ALTER TABLE SHAREDTICKETS ADD COLUMN PICKUPID SMALLINT DEFAULT 0 NOT NULL;

ALTER TABLE STOCKDIARY ADD COLUMN APPUSER VARCHAR(256);

-- RECREATE Resources table (Required for script changes)
DROP TABLE RESOURCES;
CREATE TABLE RESOURCES (
    ID VARCHAR(256) NOT NULL,
    NAME VARCHAR(1024) NOT NULL,
    RESTYPE INTEGER NOT NULL,
    CONTENT BLOB,
    PRIMARY KEY (ID)
);

CREATE UNIQUE INDEX RESOURCES_NAME_INX ON RESOURCES(NAME);

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('0', 'Menu.Root', 0, $FILE{/com/openbravo/pos/templates/Menu.Root.txt});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('1', 'coin.2', 1, $FILE{/com/openbravo/pos/templates/coin.2.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('2', 'coin.1', 1, $FILE{/com/openbravo/pos/templates/coin.1.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('3', 'coin.50', 1, $FILE{/com/openbravo/pos/templates/coin.50.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('4', 'coin.20', 1, $FILE{/com/openbravo/pos/templates/coin.20.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('5', 'coin.10', 1, $FILE{/com/openbravo/pos/templates/coin.10.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('6', 'coin.05', 1, $FILE{/com/openbravo/pos/templates/coin.05.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('7', 'coin.02', 1, $FILE{/com/openbravo/pos/templates/coin.02.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('8', 'coin.01', 1, $FILE{/com/openbravo/pos/templates/coin.01.png});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('9', 'img.cash', 1, $FILE{/com/openbravo/pos/templates/img.cash.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('10', 'img.cashdrawer', 1, $FILE{/com/openbravo/pos/templates/img.cashdrawer.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('11', 'img.discount', 1, $FILE{/com/openbravo/pos/templates/img.discount.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('12', 'img.empty', 1, $FILE{/com/openbravo/pos/templates/img.empty.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('13', 'img.heart', 1, $FILE{/com/openbravo/pos/templates/img.heart.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('14', 'img.no_photo', 1, $FILE{/com/openbravo/pos/templates/img.no_photo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('15', 'img.kit_print', 1, $FILE{/com/openbravo/pos/templates/img.kit_print.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('16', 'img.refundit', 1, $FILE{/com/openbravo/pos/templates/img.refundit.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('17', 'img.run_script', 1, $FILE{/com/openbravo/pos/templates/img.run_script.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('18', 'img.ticket_print', 1, $FILE{/com/openbravo/pos/templates/img.ticket_print.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('19', 'img.user', 1, $FILE{/com/openbravo/pos/templates/img.user.png});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('20', 'note.50', 1, $FILE{/com/openbravo/pos/templates/note.50.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('21', 'note.20', 1, $FILE{/com/openbravo/pos/templates/note.20.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('22', 'note.10', 1, $FILE{/com/openbravo/pos/templates/note.10.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('23', 'note.5', 1, $FILE{/com/openbravo/pos/templates/note.5.png});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('24', 'payment.cash', 0, $FILE{/com/openbravo/pos/templates/payment.cash.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('25', 'Printer.CloseCash', 0, $FILE{/com/openbravo/pos/templates/Printer.CloseCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('26', 'Printer.CustomerPaid', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('27', 'Printer.CustomerPaid2', 0, $FILE{/com/openbravo/pos/templates/Printer.CustomerPaid2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('28', 'Printer.FiscalTicket', 0, $FILE{/com/openbravo/pos/templates/Printer.FiscalTicket.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('29', 'Printer.Inventory', 0, $FILE{/com/openbravo/pos/templates/Printer.Inventory.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('30', 'Printer.OpenDrawer', 0, $FILE{/com/openbravo/pos/templates/Printer.OpenDrawer.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('31', 'Printer.PartialCash', 0, $FILE{/com/openbravo/pos/templates/Printer.PartialCash.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('32', 'Printer.Product', 0, $FILE{/com/openbravo/pos/templates/Printer.Product.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('33', 'Printer.Start', 0, $FILE{/com/openbravo/pos/templates/Printer.Start.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('34', 'Printer.Ticket', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('35', 'Printer.Ticket2', 0, $FILE{/com/openbravo/pos/templates/Printer.Ticket2.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('36', 'Printer.TicketClose', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketClose.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('37', 'Printer.TicketKitchen', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketKitchen.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('38', 'Printer.TicketLine', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketLine.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('39', 'Printer.TicketPreview', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketPreview.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('40', 'Printer.TicketTotal', 0, $FILE{/com/openbravo/pos/templates/Printer.TicketTotal.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('41', 'Printer.Ticket.Logo', 1, $FILE{/com/openbravo/pos/templates/Printer.Ticket.Logo.png});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('42', 'script.AddLineNote', 0, $FILE{/com/openbravo/pos/templates/script.AddLineNote.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('43', 'script.Event.Total', 0, $FILE{/com/openbravo/pos/templates/script.Event.Total.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('44', 'script.linediscount', 0, $FILE{/com/openbravo/pos/templates/script.linediscount.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('45', 'script.ReceiptConsolidate', 0, $FILE{/com/openbravo/pos/templates/script.ReceiptConsolidate.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('46', 'script.Refundit', 0, $FILE{/com/openbravo/pos/templates/script.Refundit.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('47', 'script.SendOrder', 0, $FILE{/com/openbravo/pos/templates/script.SendOrder.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('48', 'script.ServiceCharge', 0, $FILE{/com/openbravo/pos/templates/script.script.ServiceCharge.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('49', 'script.SetPerson', 0, $FILE{/com/openbravo/pos/templates/script.SetPerson.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('50', 'script.StockCurrentAdd', 0, $FILE{/com/openbravo/pos/templates/script.StockCurrentAdd.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('51', 'script.StockCurrentSet', 0, $FILE{/com/openbravo/pos/templates/script.StockCurrentSet.txt});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('52', 'script.totaldiscount', 0, $FILE{/com/openbravo/pos/templates/script.totaldiscount.txt});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('53', 'Ticket.Buttons', 0, $FILE{/com/openbravo/pos/templates/Ticket.Buttons.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('54', 'Ticket.Close', 0, $FILE{/com/openbravo/pos/templates/Ticket.Close.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('55', 'Ticket.Discount', 0, $FILE{/com/openbravo/pos/templates/Ticket.Discount.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('56', 'Ticket.Line', 0, $FILE{/com/openbravo/pos/templates/Ticket.Line.xml});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('57', 'Ticket.TicketLineTaxesIncluded', 0, $FILE{/com/openbravo/pos/templates/Ticket.TicketLineTaxesIncluded.xml});

INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('58', 'Window.Logo', 1, $FILE{/com/openbravo/pos/templates/Window.Logo.png});
INSERT INTO RESOURCES(ID, NAME, RESTYPE, CONTENT) VALUES('59', 'Window.Title', 0, $FILE{/com/openbravo/pos/templates/Window.Title.txt});

-- UPDATE App' version
UPDATE APPLICATIONS SET NAME = $APP_NAME{}, VERSION = $APP_VERSION{} WHERE ID = $APP_ID{};

-- final script
DELETE FROM SHAREDTICKETS;
