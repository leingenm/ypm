package com.ypm.service.mapper;

import com.google.api.services.youtube.model.PlaylistItem;
import com.ypm.dto.PlayListItemDto;

public class PlaylistItemMapper {
    public static PlayListItemDto mapToDto(PlaylistItem playlistItem) {
        var snippet = playlistItem.getSnippet();

        var thumbnailHigh = snippet.getThumbnails().getHigh();
        var thumbnailStandard = snippet.getThumbnails().getStandard();
        String thumbnailUrl = "";

        if (thumbnailHigh != null) {
            thumbnailUrl = thumbnailHigh.getUrl();
        } else if (thumbnailStandard != null) {
            thumbnailUrl = thumbnailStandard.getUrl();
        }

        return new PlayListItemDto(
            snippet.getTitle(),
            snippet.getVideoOwnerChannelTitle(),
            snippet.getResourceId().getVideoId(),
            thumbnailUrl
        );
    }
}
