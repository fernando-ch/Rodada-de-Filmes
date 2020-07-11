
-- drop table recomendacoes;
-- drop table rodadas;
-- drop table pessoas;

create table people (
    id   integer not null primary key,
    name text    not null unique
);

insert into people values (1, 'Fernando');
insert into people values (2, 'Fabiane');
insert into people values (3, 'Felipe');
insert into people values (4, 'Bianca');
insert into people values (5, 'Vitor');
insert into people values (6, 'Thayanne');
insert into people values (7, 'Lucas');

create TYPE round_step AS ENUM ('Recommendation', 'WhoSawWhat');

create table rounds (
    id serial not null primary key,
    current boolean not null,
    step text not null
);

insert into rounds (current, step) values (true, 'Recommendation');

create table movies (
    id serial not null primary key,
    person_id integer not null references people(id),
    round_id integer not null references rounds(id),
    title text not null unique,

    unique (person_id, round_id)
);

create table movies_visualizations (
    id                          serial  not null primary key,
    movie_id                    integer not null references movies(id),
    person_id                   integer not null references people(id),
    already_saw_before_round    boolean not null,
    already_saw_during_round    boolean not null,

    unique (movie_id, person_id)
)

-- insert into recommendations (person_id, round_id, title) values (2, 1, 'Corra');
-- insert into recommendations (person_id, round_id, title) values (3, 1, 'Exterminador');
-- insert into recommendations (person_id, round_id, title) values (4, 1, 'Coringa');
-- insert into recommendations (person_id, round_id, title) values (5, 1, 'Chamado');
-- insert into recommendations (person_id, round_id, title) values (6, 1, 'Pirula');
-- insert into recommendations (person_id, round_id, title) values (7, 1, 'Coisas loucas');
