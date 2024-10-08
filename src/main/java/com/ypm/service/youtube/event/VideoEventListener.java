package com.ypm.service.youtube.event;

import com.ypm.constant.ProcessingStatus;
import com.ypm.persistence.entity.VideoData;
import com.ypm.persistence.event.VideosSavedEvent;
import com.ypm.persistence.repository.VideoRepository;
import com.ypm.service.BatchStatusManager;
import com.ypm.service.youtube.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
@AllArgsConstructor
public class VideoEventListener {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleVideosSavedEvent(VideosSavedEvent event) {
        try {
            var videoDtoList = videoService.getVideoData(event.getVideoIds());

            var videosToSave = videoDtoList.stream()
                                           .filter(dto -> !dto.title().isEmpty() && !dto.description().isEmpty())
                                           .map(dto -> videoRepository.findVideoByYoutubeId(dto.id()).map(video -> {
                                               String tagsAsString = "";
                                               if (dto.tags() != null) {
                                                   tagsAsString = String.join(", ", dto.tags());
                                               }

                                               var videoData = new VideoData(dto.title(), dto.description(), dto.channelName(), tagsAsString);
                                               video.setVideoData(videoData);

                                               if (video.getVideoData() != null) {
                                                   video.getVideoData().setVideo(video);
                                               }

                                               return video;
                                           }).orElse(null)).filter(Objects::nonNull).toList();

            videoRepository.saveAll(videosToSave);
            BatchStatusManager.updateBatchStatus(event.processingId(), ProcessingStatus.COMPLETED);
        } catch (Exception e) {
            BatchStatusManager.updateBatchStatus(event.processingId(), ProcessingStatus.FAILED, event.getVideoIds(), e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
