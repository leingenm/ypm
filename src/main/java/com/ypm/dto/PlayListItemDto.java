package com.ypm.dto;

public record PlayListItemDto(
    String videoTitle,
    String videoOwnerChannelTitle,
    String videoId,
    String videoThumbnail
) {
}
