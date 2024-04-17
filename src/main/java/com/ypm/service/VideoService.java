package com.ypm.service;

import com.google.api.services.youtube.model.PlaylistItem;

import java.io.IOException;
import java.util.List;

public interface VideoService {

    List<PlaylistItem> moveVideos(String accessToken,
                                  String playListId,
                                  String targetPlayListId,
                                  List<String> videosIds) throws IOException;

    List<PlaylistItem> getVideos(String accessToken,
                                 List<String> videosIds) throws IOException;

    List<PlaylistItem> getPlayListVideos(String accessToken,
                                         String playListId) throws IOException;

    PlaylistItem insertVideo(String accessToken, PlaylistItem playlistItem) throws IOException;

    void deleteVideo(String accessToken, String videoId) throws IOException;
}
