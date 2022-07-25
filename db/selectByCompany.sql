CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE companySerial START 1;

INSERT INTO company (id, name)
    VALUES
    (nextval('companySerial'), 'Coca'),
    (nextval('companySerial'), 'Snickers'),
    (nextval('companySerial'), 'IBM'),
    (nextval('companySerial'), 'Apple'),
    (nextval('companySerial'), 'JetBrains'),
    (nextval('companySerial'), 'Sber');

CREATE SEQUENCE personSerial START 1000;

INSERT INTO person (id, name, company_id)
    VALUES
    (nextval('personSerial'), 'Semen', 1),
    (nextval('personSerial'), 'Idris', 1),
    (nextval('personSerial'), 'Bob', 1),
    (nextval('personSerial'), 'Tom', 1),
    (nextval('personSerial'), 'Jack', 2),
    (nextval('personSerial'), 'Irina', 2),
    (nextval('personSerial'), 'Andrew', 3),
    (nextval('personSerial'), 'John', 3),
    (nextval('personSerial'), 'Anton', 3),
    (nextval('personSerial'), 'Roman', 3),
    (nextval('personSerial'), 'Maks', 3),
    (nextval('personSerial'), 'Bob', 4),
    (nextval('personSerial'), 'George', 4),
    (nextval('personSerial'), 'Alisa', 4),
    (nextval('personSerial'), 'Tamara', 4),
    (nextval('personSerial'), 'Sulim', 5),
    (nextval('personSerial'), 'Petr', 5),
    (nextval('personSerial'), 'Stas', 5),
    (nextval('personSerial'), 'Alexey', 5),
    (nextval('personSerial'), 'Lana', 5),
    (nextval('personSerial'), 'Andrey', 5),
    (nextval('personSerial'), 'Oleg', 5),
    (nextval('personSerial'), 'Nikita', 6),
    (nextval('personSerial'), 'Trophim', 6),
    (nextval('personSerial'), 'Liza', 6),
    (nextval('personSerial'), 'Artem', 6);

SELECT p.name, c.name
FROM person p
JOIN company c on c.id = p.company_id
WHERE company_id !=5
ORDER BY p.name;

SELECT c.name , COUNT(*)
FROM company c
JOIN person p on c.id = p.company_id
GROUP BY c.name
ORDER BY COUNT(*) DESC
LIMIT 3;

// посмотреть на имена победителей:
SELECT p.name, c.name
FROM company c
JOIN person p on c.id = p.company_id
WHERE company_id = 5;

INSERT INTO person (id, name, company_id)
    VALUES
    (nextval('personSerial'), 'Greg', 3),
    (nextval('personSerial'), 'Juda', 3);

SELECT c.name, COUNT(p.company_id) AS top_count
FROM person p
JOIN company c ON p.company_id = c.id
GROUP BY c.name
HAVING COUNT(p.company_id) = (
	SELECT MAX(cnt)
	FROM (
		SELECT COUNT(*) AS cnt
		FROM person p
		GROUP BY p.company_id) AS top_company);