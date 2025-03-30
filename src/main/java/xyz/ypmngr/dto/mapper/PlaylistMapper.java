package xyz.ypmngr.dto.mapper;

import xyz.ypmngr.model.Playlist;
import xyz.ypmngr.model.PrivacyStatus;

import java.time.Instant;
import java.time.ZoneId;

public class PlaylistMapper {

    public static Playlist map(com.google.api.services.youtube.model.Playlist ytPlaylist) {
        var publishedAtEpochMillis = ytPlaylist.getSnippet().getPublishedAt().getValue();
        var publishedAt = Instant.ofEpochMilli(publishedAtEpochMillis).atZone(ZoneId.of("UTC"));
        return new Playlist()
                .id(ytPlaylist.getId())
                .title(ytPlaylist.getSnippet().getTitle())
                .description(ytPlaylist.getSnippet().getDescription())
                .channelId(ytPlaylist.getSnippet().getChannelId())
                .channelTitle(ytPlaylist.getSnippet().getChannelTitle())
                .publishedAt(publishedAt.toLocalDate())
                .tags(ytPlaylist.getSnippet().getTags())
                .privacyStatus(PrivacyStatus.fromValue(ytPlaylist.getStatus().getPrivacyStatus().toUpperCase()));
    }
}
