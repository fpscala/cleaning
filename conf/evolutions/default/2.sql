# --- !Ups
CREATE TABLE "Counts" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  UNIQUE("name")
);

CREATE TABLE "Price_list" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "count" VARCHAR CONSTRAINT  "Price_listFkCountName" REFERENCES  "Counts" ("name") ON UPDATE CASCADE ON DELETE CASCADE,
  "price" VARCHAR NOT NULL,
  "title" VARCHAR NOT NULL,
  UNIQUE("name")
);

CREATE TABLE "Orders" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "surname" VARCHAR NOT NULL,
  "firstName" VARCHAR NOT NULL,
  "phone" VARCHAR NOT NULL,
  "address" VARCHAR NOT NULL,
  "orderDay" TIMESTAMP NOT NULL,
  "email" VARCHAR NOT NULL,
  "comment" TEXT NULL,
  "linkCode" VARCHAR(5) NOT NULL,
  "type" VARCHAR CONSTRAINT "OrdersFkPrice_listName" REFERENCES "Price_list"("name") ON update CASCADE ON DELETE CASCADE,
  "price" VARCHAR NOT NULL,
  "status_order" INTEGER NOT NULL,
  UNIQUE("linkCode")
);

INSERT INTO "Counts" ("name") VALUES ('1кг.');
INSERT INTO "Counts" ("name") VALUES ('1шт.');
INSERT INTO "Counts" ("name") VALUES ('комп.');
INSERT INTO "Counts" ("name") VALUES ('2шт.');
INSERT INTO "Counts" ("name") VALUES ('Пара.');
INSERT INTO "Counts" ("name") VALUES ('1м.');

# --- !Downs
DROP TABLE "Price_list";
DROP TABLE "Counts";
DROP TABLE "Orders";
