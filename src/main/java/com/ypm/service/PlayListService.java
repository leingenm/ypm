package com.ypm.service;

import com.google.api.services.youtube.model.Playlist;
import com.ypm.dto.PlaylistDto;

import java.io.IOException;
import java.util.List;

public interface PlayListService {

    List<Playlist> getPlayLists(String accessToken) throws IOException;

    Playlist createPlayList(String accessToken, PlaylistDto playlistDto) throws IOException;

    Playlist getPlayListById(String accessToken, String playListId) throws IOException;

    void deletePlayList(String accessToken, String playListId) throws IOException;

    Playlist mergePlayLists(String accessToken, PlaylistDto playlistDto,
                            List<String> playListsIds,
                            boolean deleteAfterMerge) throws IOException;

    Playlist updatePlayListTitle(String accessToken, String playListId,
                                 String newTitle) throws IOException;

    String exportPlaylist(String accessToken, String playlistId) throws IOException;
}
