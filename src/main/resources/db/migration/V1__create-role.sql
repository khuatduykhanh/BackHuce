DROP TABLE IF EXISTS "public"."roles";
CREATE TABLE roles (
                                 "id" int PRIMARY KEY,
                                  "name" varchar(255) NOT NULL
);
INSERT INTO "public"."roles" VALUES(1,'ROLE_ADMIN');
INSERT INTO "public"."roles" VALUES(2,'ROLE_USER');