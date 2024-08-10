package com.ypm.service.mapper;

import com.google.api.services.youtube.model.Video;
import com.ypm.dto.VideoDto;

import java.util.List;
import java.util.stream.Collectors;

public class VideoMapper {

    public static List<VideoDto> mapToVideoDto(List<Video> videos) {
        return videos.stream()
                     .map(video -> {
                         var snippet = video.getSnippet();
                         return new VideoDto(
                             video.getId(),
                             snippet.getTitle(),
                             snippet.getDescription(),
                             snippet.getTags(),
                             snippet.getChannelTitle()
                         );
                     })
                     .collect(Collectors.toList());
    }
}
