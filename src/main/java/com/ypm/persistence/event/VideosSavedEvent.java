package com.ypm.persistence.event;

import com.ypm.persistence.entity.Video;

import java.util.List;

public record VideosSavedEvent(List<Video> videos) {

    public List<String> getVideoIds() {
        return videos.stream().map(Video::getYoutubeId).toList();
    }
}
