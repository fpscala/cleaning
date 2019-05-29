# --- !Ups
CREATE TABLE "GenderCodes" ("id" SERIAL NOT NULL PRIMARY KEY,"gender" VARCHAR NOT NULL);
CREATE TABLE "EducationCodes" ("id" SERIAL NOT NULL PRIMARY KEY,"education" VARCHAR NOT NULL);

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
  "itn" INTEGER NOT NULL,
  "gender" INTEGER CONSTRAINT "workersFkGenderCodeId" REFERENCES "GenderCodes" ON update CASCADE ON DELETE CASCADE,
  "birthDay" TIMESTAMP NOT NULL,
  "birthPlace" VARCHAR NOT NULL,
  "education" INTEGER CONSTRAINT  "educationFkEducationCodeId" REFERENCES  "EducationCodes" ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE ("passportSeriesAndNumber", "pensionNumber", "itn")
);

INSERT INTO "GenderCodes" ("gender") VALUES ('erkak');
INSERT INTO "GenderCodes" ("gender") VALUES ('ayol');

INSERT INTO "EducationCodes" ("education") VALUES ('yoq');
INSERT INTO "EducationCodes" ("education") VALUES ('orta');
INSERT INTO "EducationCodes" ("education") VALUES ('oliy');

# --- !Downs
DROP TABLE "Workers";
DROP TABLE "EducationCodes";
DROP TABLE "GenderCodes";
