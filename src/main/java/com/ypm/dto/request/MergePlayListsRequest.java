package com.ypm.dto.request;

import java.util.List;

public record MergePlayListsRequest(String mergedPlayListTitle,
                                    List<String> playListsIds,
                                    boolean deleteAfterMerge) {
}
