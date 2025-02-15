--liquibase formatted sql

-- changeset Roman Maher:1
CREATE TABLE IF NOT EXISTS playlists
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    description TEXT,
    status      TEXT
);

-- changeset Roman Maher:2
CREATE TABLE IF NOT EXISTS videos
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    youtube_id  TEXT UNIQUE           NOT NULL,
    import_date DATE,
    playlist_id BIGSERIAL             NOT NULL,

    CONSTRAINT fk_playlist_id_playlists FOREIGN KEY (playlist_id) REFERENCES playlists (id),
    CONSTRAINT unq UNIQUE (youtube_id)
);

-- changeset Roman Maher:3
CREATE TABLE IF NOT EXISTS video_data
(
    id           BIGSERIAL PRIMARY KEY NOT NULL,
    title        TEXT,
    description  TEXT,
    channel_name TEXT,
    tags         TEXT,
    video_id     BIGSERIAL             NOT NULL,

    CONSTRAINT fk_video_id_videos FOREIGN KEY (video_id) REFERENCES videos (id) ON DELETE CASCADE,
    CONSTRAINT uc_video_data_video UNIQUE (video_id)
);

-- changeset Roman:insert-playlist-1
INSERT INTO playlists (id, name, description)
VALUES (1, 'Watch Later', 'Thing to watch later on');
