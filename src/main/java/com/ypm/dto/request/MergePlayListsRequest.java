package com.ypm.dto.request;

import com.ypm.dto.PlaylistDto;

import java.util.List;

public record MergePlayListsRequest(PlaylistDto playlistDto,
                                    List<String> playListsIds,
                                    boolean deleteAfterMerge) { }
