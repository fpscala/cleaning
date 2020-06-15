CREATE TABLE "GenderCodes" ("id" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL, "code" VARCHAR NOT NULL, UNIQUE(code));
CREATE TABLE "EducationCodes" ("id" SERIAL NOT NULL PRIMARY KEY,"name" VARCHAR NOT NULL, "code" VARCHAR NOT NULL, UNIQUE(code));
CREATE TABLE "Role" ("id" SERIAL NOT NULL PRIMARY KEY, "role" VARCHAR NOT NULL, UNIQUE(role));

CREATE TABLE "Workers" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "surname" VARCHAR NOT NULL,
  "firstName" VARCHAR NOT NULL,
  "lastName" VARCHAR NULL,
  "address" VARCHAR NOT NULL,
  "phone" VARCHAR NOT NULL,
  "passportSeriesAndNumber" VARCHAR NOT NULL,
  "dayGettingPassport" TIMESTAMP NOT NULL,
  "photoName" VARCHAR NOT NULL,
  "photoHash" VARCHAR NOT NULL,
  "warnings" jsonb NULL,
  "pensionNumber" INTEGER NULL,
  "itn" BIGINT NOT NULL,
  "genderId" INTEGER CONSTRAINT "workersFkGenderCodeId" REFERENCES "GenderCodes" ON update CASCADE ON DELETE CASCADE,
  "birthDay" TIMESTAMP NOT NULL,
  "birthPlace" VARCHAR NOT NULL,
  "educationId" INTEGER CONSTRAINT  "educationFkEducationCodeId" REFERENCES  "EducationCodes" ON UPDATE CASCADE ON DELETE CASCADE,
  "password" VARCHAR NOT NULL,
  "role" INTEGER CONSTRAINT  "roleFkRoleId" REFERENCES  "Role" ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE ("passportSeriesAndNumber", "pensionNumber", "itn", "password")
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

INSERT INTO "GenderCodes" ("name", "code") VALUES ('erkak', 'male');
INSERT INTO "GenderCodes" ("name", "code") VALUES ('ayol', 'female');

INSERT INTO "EducationCodes" ("name", "code") VALUES ('yoq', 'absent');
INSERT INTO "EducationCodes" ("name", "code") VALUES ('orta', 'secondary');
INSERT INTO "EducationCodes" ("name", "code") VALUES ('oliy', 'high');

INSERT INTO "Role" ("role") VALUES ('Worker');
INSERT INTO "Role" ("role") VALUES ('Manager');
CREATE TABLE "Counts" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  UNIQUE("name")
);


INSERT INTO "Counts" ("name") VALUES ('1кг.');
INSERT INTO "Counts" ("name") VALUES ('1шт.');
INSERT INTO "Counts" ("name") VALUES ('комп.');
INSERT INTO "Counts" ("name") VALUES ('2шт.');
INSERT INTO "Counts" ("name") VALUES ('Пара.');
INSERT INTO "Counts" ("name") VALUES ('1м.');