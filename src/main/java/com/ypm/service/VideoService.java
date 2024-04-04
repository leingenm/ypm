package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final YouTube youTubeClient;

    public List<PlaylistItem> moveVideos(String accessToken,
                                         String playListId,
                                         String targetPlayListId,
                                         List<String> videosIds) throws IOException {

        List<PlaylistItem> videosToMove = getVideos(accessToken, videosIds);
        List<PlaylistItem> movedVideos = new ArrayList<>(videosIds.size());

        for (PlaylistItem video : videosToMove) {
            if (isVideoInPlayList(playListId, video)) {
                video.getSnippet().setPlaylistId(targetPlayListId);
                movedVideos.add(insertVideo(accessToken, video));
                deleteVideo(accessToken, video.getId());
            }
        }

        return movedVideos;
    }

    public List<PlaylistItem> getVideos(String accessToken,
                                        List<String> videosIds) throws IOException {

        return youTubeClient
            .playlistItems()
            .list(List.of("snippet"))
            .setId(videosIds)
            .setAccessToken(accessToken)
            .execute()
            .getItems();
    }

    public PlaylistItem insertVideo(String accessToken, PlaylistItem playlistItem) throws IOException {
        return youTubeClient
            .playlistItems()
            .insert(List.of("snippet"), playlistItem)
            .setAccessToken(accessToken)
            .execute();
    }

    public void deleteVideo(String accessToken, String videoId) throws IOException {
        youTubeClient
            .playlistItems()
            .delete(videoId)
            .setAccessToken(accessToken)
            .execute();
    }

    public List<PlaylistItem> getPlayListVideos(String accessToken,
                                                String playListId) throws IOException {

        return youTubeClient
            .playlistItems()
            .list(List.of("snippet"))
            .setPlaylistId(playListId)
            .setAccessToken(accessToken)
            .execute()
            .getItems();
    }

    private boolean isVideoInPlayList(String playListId, PlaylistItem video) {
        return video
            .getSnippet()
            .getPlaylistId()
            .equals(playListId);
    }
}
