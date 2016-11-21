alter table person
  add column email varchar not null default '',
  add column password_hash varchar not null default '',
  add column is_admin boolean not null default false;

