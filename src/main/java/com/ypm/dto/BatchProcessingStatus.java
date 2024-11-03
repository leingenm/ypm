package com.ypm.dto;

import com.ypm.constant.ProcessingStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public final class BatchProcessingStatus {

    @Setter
    private ProcessingStatus status;

    @Setter
    private String errorMessage;

    private List<String> failedVideoIds;

    public BatchProcessingStatus(ProcessingStatus status) {
        this.status = status;
        this.failedVideoIds = new ArrayList<>();
    }

    public void addFailedVideoId(String videoId) {
        failedVideoIds.add(videoId);
    }

    public void addFailedVideoIds(List<String> videoIds) {
        failedVideoIds.addAll(videoIds);
    }
}
