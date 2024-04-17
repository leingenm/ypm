package com.ypm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ypm.dto.PlaylistDto;

import java.util.List;

public record MergePlayListsRequest(@JsonProperty(value = "playlistDetails") PlaylistDto playlistDto,
                                    List<String> playListsIds,
                                    boolean deleteAfterMerge) { }
