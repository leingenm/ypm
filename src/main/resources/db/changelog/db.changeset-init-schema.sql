--liquibase formatted sql

-- changeset Roman Maher:1
CREATE TABLE IF NOT EXISTS playlists
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    name        VARCHAR(255)          NOT NULL,
    description TEXT,
    status      VARCHAR
);

-- changeset Roman Maher:2
CREATE TABLE IF NOT EXISTS videos
(
    id          BIGSERIAL PRIMARY KEY NOT NULL,
    youtube_id  VARCHAR UNIQUE        NOT NULL,
    import_date DATE,
    playlist_id BIGSERIAL             NOT NULL,

    CONSTRAINT fk_playlist_id_playlists FOREIGN KEY (playlist_id) REFERENCES playlists (id),
    CONSTRAINT unq UNIQUE (youtube_id)
);

-- changeset Roman Maher:3
CREATE TABLE IF NOT EXISTS video_data
(
    id           BIGSERIAL PRIMARY KEY NOT NULL,
    title        VARCHAR,
    description  TEXT,
    channel_name VARCHAR,
    tags         VARCHAR,
    video_id     BIGSERIAL             NOT NULL,

    CONSTRAINT fk_video_id_videos FOREIGN KEY (video_id) REFERENCES videos (id) ON DELETE CASCADE
);
