package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.ypm.constant.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImp implements VideoService {

    private final YouTube youTubeClient;

    @Override
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

    @Override
    public List<PlaylistItem> getVideos(String accessToken,
                                        List<String> videosIds) throws IOException {

        return youTubeClient
            .playlistItems()
            .list(List.of(Part.SNIPPET.toString()))
            .setId(videosIds)
            .setAccessToken(accessToken)
            .execute()
            .getItems();
    }

    @Override
    public PlaylistItem insertVideo(String accessToken, PlaylistItem playlistItem) throws IOException {
        return youTubeClient
            .playlistItems()
            .insert(List.of(Part.SNIPPET.toString()), playlistItem)
            .setAccessToken(accessToken)
            .execute();
    }

    @Override
    public void deleteVideo(String accessToken, String videoId) throws IOException {
        youTubeClient
            .playlistItems()
            .delete(videoId)
            .setAccessToken(accessToken)
            .execute();
    }

    @Override
    public List<PlaylistItem> getPlayListVideos(String accessToken,
                                                String playListId) throws IOException {

        return youTubeClient
            .playlistItems()
            .list(List.of(Part.SNIPPET.toString()))
            .setPlaylistId(playListId)
            .setAccessToken(accessToken)
            .execute()
            .getItems();
    }

    // TODO: Update naming & Merge with method above
    public List<PlaylistItem> getPlaylistVideosPagination(String accessToken, String playlistId) throws IOException {
        List<PlaylistItem> allItems = new ArrayList<>();
        String nextPageToken = null;

        do {
            var request = youTubeClient
                .playlistItems()
                .list(List.of(Part.SNIPPET.toString()))
                .setPlaylistId(playlistId)
                .setMaxResults(50L)
                .setPageToken(nextPageToken)
                .setAccessToken(accessToken)
                .execute();

            allItems.addAll(request.getItems());
            nextPageToken = request.getNextPageToken();
        } while (nextPageToken != null);

        return allItems;
    }

    private boolean isVideoInPlayList(String playListId, PlaylistItem video) {
        return video
            .getSnippet()
            .getPlaylistId()
            .equals(playListId);
    }
}
