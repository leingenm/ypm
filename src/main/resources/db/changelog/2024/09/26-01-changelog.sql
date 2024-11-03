-- liquibase formatted sql

-- changeset Roman:insert-playlist-1
INSERT INTO ypm.playlists (id, name, description)
VALUES (1, 'Watch Later', 'Watch Later');
