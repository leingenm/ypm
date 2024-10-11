package com.ypm.service.youtube.event;

import com.ypm.constant.ProcessingStatus;
import com.ypm.dto.VideoDto;
import com.ypm.persistence.entity.Video;
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
                    .filter(this::isValidDtoData)
                    .map(dto -> videoRepository.findVideoByYoutubeId(dto.id())
                                               .map(video -> addOrUpdateVideoData(dto, video))
                                               .orElse(null))
                    .filter(Objects::nonNull)
                    .toList();

            videoRepository.saveAll(videosToSave);
            BatchStatusManager.updateBatchStatus(event.processingId(), ProcessingStatus.COMPLETED);
        } catch (Exception e) {
            BatchStatusManager.updateBatchStatus(event.processingId(), ProcessingStatus.FAILED, event.getVideoIds(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Video addOrUpdateVideoData(VideoDto dto, Video video) {
        if (video.getVideoData() == null) {
            var videoData = new VideoData(dto.title(), dto.description(), dto.channelName(), this.getTags(dto));
            video.setVideoData(videoData);
            video.getVideoData().setVideo(video);
        } else if (hasVideoDataChanged(video.getVideoData(), dto)) {
            updateVideoData(video.getVideoData(), dto);
        }

        return video;
    }

    private boolean isValidDtoData(VideoDto dto) {
        return !dto.title().isEmpty() && !dto.description().isEmpty();
    }

    private boolean hasVideoDataChanged(VideoData videoData, VideoDto dto) {
        return !Objects.equals(videoData.getTitle(), dto.title())
                || !Objects.equals(videoData.getDescription(), dto.description())
                || !Objects.equals(videoData.getChannelName(), dto.channelName())
                || !Objects.equals(videoData.getTags(), this.getTags(dto));
    }

    private void updateVideoData(VideoData videoData, VideoDto videoDto) {
        videoData.setTitle(videoDto.title());
        videoData.setDescription(videoDto.description());
        videoData.setChannelName(videoDto.channelName());
        videoData.setTags(this.getTags(videoDto));
    }

    private String getTags(VideoDto dto) {
        return dto.tags() != null ? tagsToString(dto) : null;
    }

    private String tagsToString(VideoDto videoDto) {
        return String.join(", ", videoDto.tags());
    }
}
