-- liquibase formatted sql

-- changeset Roman:1726771483947-6
ALTER TABLE ypm.video_data
    ADD CONSTRAINT uc_video_data_video UNIQUE (video_id);

-- changeset Roman:1726771483947-1
ALTER TABLE ypm.video_data
    ALTER COLUMN channel_name TYPE TEXT USING (channel_name::TEXT);

-- changeset Roman:1726771483947-2
ALTER TABLE ypm.playlists
    ALTER COLUMN status TYPE TEXT USING (status::TEXT);

-- changeset Roman:1726771483947-3
ALTER TABLE ypm.video_data
    ALTER COLUMN tags TYPE TEXT USING (tags::TEXT);

-- changeset Roman:1726771483947-4
ALTER TABLE ypm.video_data
    ALTER COLUMN title TYPE TEXT USING (title::TEXT);

-- changeset Roman:1726771483947-5
ALTER TABLE ypm.videos
    ALTER COLUMN youtube_id TYPE TEXT USING (youtube_id::TEXT);
