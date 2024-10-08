package com.ypm.service;

import com.ypm.constant.ProcessingStatus;
import com.ypm.dto.BatchProcessingStatus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class BatchStatusManager {

    private final static Map<String, BatchProcessingStatus> BATCH_STATUS_MAP = new ConcurrentHashMap<>();

    public static void updateBatchStatus(String processingId, ProcessingStatus status, List<String> failedVideoIds, String... errorMessage) {
        var currentStatus = BATCH_STATUS_MAP.get(processingId);

        if (currentStatus == null) {
            currentStatus = new BatchProcessingStatus(status);
        } else {
            currentStatus.setStatus(status);

            if (errorMessage != null && errorMessage.length > 0) {
                currentStatus.setErrorMessage(errorMessage[0]);
            }

            if (failedVideoIds != null && !failedVideoIds.isEmpty()) {
                currentStatus.addFailedVideoIds(failedVideoIds);
            }
        }

        BATCH_STATUS_MAP.put(processingId, currentStatus);
    }

    public static void updateBatchStatus(String processingId, ProcessingStatus status, String... errorMessage) {
        updateBatchStatus(processingId, status, null, errorMessage);
    }

    public static BatchProcessingStatus getBatchProcessingStatus(String processingId) {
        return BATCH_STATUS_MAP.getOrDefault(processingId, new BatchProcessingStatus(ProcessingStatus.FAILED));
    }

    public static Map<String, BatchProcessingStatus> getBatchProcessingStatuses() {
        return new ConcurrentHashMap<>(BATCH_STATUS_MAP);
    }
}
