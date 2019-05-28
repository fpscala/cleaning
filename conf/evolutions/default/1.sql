# --- !Ups
CREATE TABLE "users" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "login" VARCHAR(15) NOT NULL,
  "name" VARCHAR(20) NOT NULL,
  "password" VARCHAR(50) NOT NULL
);

# --- !Downs
DROP TABLE "users";
