package xyz.ypmngr.persistence.event;

import xyz.ypmngr.persistence.entity.Video;

import java.util.List;

public record VideosSavedEvent(String processingId, List<Video> videos) {

    public List<String> getVideoIds() {
        return videos.stream().map(Video::getYoutubeId).toList();
    }
}
