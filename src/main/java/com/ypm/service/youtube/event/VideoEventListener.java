package com.ypm.service.youtube.event;

import com.ypm.persistence.entity.VideoData;
import com.ypm.persistence.event.VideosSavedEvent;
import com.ypm.persistence.repository.VideoRepository;
import com.ypm.service.youtube.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@AllArgsConstructor
public class VideoEventListener {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleVideosSavedEvent(VideosSavedEvent videosSavedEvent) throws IOException {
        var videoDtoList = videoService.getVideoData(videosSavedEvent.getVideoIds());

        videoDtoList.forEach(videoDto -> {
            if (videoDto.title().isEmpty() || videoDto.description().isEmpty()) return;

            var optionalVideo = videoRepository.findVideoByYoutubeId(videoDto.id());

            optionalVideo.ifPresent(video -> {
                String tagsAsString = "";
                if (videoDto.tags() != null) {
                    tagsAsString = String.join(", ", videoDto.tags());
                }

                var videoData = new VideoData(
                    videoDto.title(),
                    videoDto.description(),
                    videoDto.channelName(), tagsAsString);

                video.setVideoData(videoData);

                if (video.getVideoData() != null) {
                    video.getVideoData().setVideo(video);
                }

                videoRepository.save(video);
            });
        });
    }
}
