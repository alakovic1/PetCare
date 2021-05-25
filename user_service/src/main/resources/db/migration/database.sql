create table if not exists public.questions
(
    id bigint not null
    constraint questions_pkey
    primary key,
    description text,
    title varchar(100)
);

create table if not exists public.roles
(
    id bigint not null
    constraint roles_pkey
    primary key,
    name varchar(60)
);

create table if not exists public.answers
(
    id bigint not null
    constraint answers_pkey
    primary key,
    text text,
    question_id bigint not null
    constraint fk3erw1a3t0r78st8ty27x6v3g1
    references questions
    on delete cascade
);

create table if not exists public.users
(
    id bigint not null
    constraint users_pkey
    primary key,
    email varchar(100),
    name text,
    surname text,
    password varchar(255),
    username varchar(40),
    answer_id bigint not null
    constraint uk_50cb9w7mvchw2sy7jx9hqo4fy
    unique
    constraint fkonb2fpet6u22f0dim0k0xq3ou
    references answers
    on delete cascade
);

create table if not exists public.user_roles
(
    user_id bigint not null
    constraint fkhfh9dx7w3ubf1co1vdev94g3f
    references users,
    role_id bigint not null
    constraint fkh8ciramu9cc9q3qcqiv4ue8a6
    references roles,
    constraint user_roles_pkey
    primary key (user_id, role_id)
);