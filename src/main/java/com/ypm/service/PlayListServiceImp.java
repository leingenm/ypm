package com.ypm.service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;
import com.ypm.constant.Part;
import com.ypm.constant.PrivacyStatus;
import com.ypm.dto.PlaylistDto;
import com.ypm.exception.PlayListNotFoundException;
import com.ypm.service.helper.CsvWriter;
import com.ypm.service.mapper.PlaylistItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayListServiceImp implements PlayListService {

    private final YouTube youTubeClient;
    private final VideoServiceImp videoService;

    @Override
    public List<Playlist> getPlayLists(String accessToken) throws IOException {
        return youTubeClient
            .playlists()
            .list(List.of(Part.SNIPPET.toString()))
            .setMaxResults(50L)
            .setAccessToken(accessToken)
            .setMine(true)
            .execute()
            .getItems();
    }

    @Override
    public Playlist mergePlayLists(String accessToken, PlaylistDto playlistDto,
                                   List<String> playListsIds,
                                   boolean deleteAfterMerge) throws IOException {

        var mergedPlayList = createPlayList(accessToken, playlistDto);

        for (var playListId : playListsIds) {
            var videos = videoService.getPlayListVideos(accessToken, playListId);
            var videosIds = videos.stream().map(PlaylistItem::getId).toList();
            videoService.moveVideos(accessToken, playListId, mergedPlayList.getId(), videosIds);
            if (deleteAfterMerge) deletePlayList(accessToken, playListId);
        }

        return mergedPlayList;
    }

    @Override
    public Playlist createPlayList(String accessToken, PlaylistDto playlistDto) throws IOException {
        var status = playlistDto.status().isEmpty()
            ? PrivacyStatus.PRIVATE.toString()
            : playlistDto.status();

        var playlistStatus = new PlaylistStatus()
            .setPrivacyStatus(status);

        var playlistSnippet = new PlaylistSnippet()
            .setTitle(playlistDto.title())
            .setDescription(playlistDto.description().orElse(null));

        var playlist = new Playlist()
            .setSnippet(playlistSnippet)
            .setStatus(playlistStatus);

        return youTubeClient
            .playlists()
            .insert(List.of(Part.STATUS.toString(), Part.SNIPPET.toString()), playlist)
            .setAccessToken(accessToken)
            .execute();
    }

    @Override
    public Playlist updatePlayListTitle(String accessToken, String playListId,
                                        String newTitle) throws IOException {

        var playlistToEdit = getPlayListById(accessToken, playListId);
        playlistToEdit.getSnippet().setTitle(newTitle);

        return youTubeClient
            .playlists()
            .update(List.of(Part.SNIPPET.toString()), playlistToEdit)
            .setAccessToken(accessToken)
            .execute();
    }

    @Override
    public Playlist getPlayListById(String accessToken, String playListId) throws IOException {
        return youTubeClient
            .playlists()
            .list(List.of(Part.SNIPPET.toString()))
            .setId(List.of(playListId))
            .setAccessToken(accessToken)
            .execute()
            .getItems()
            .stream()
            .findFirst()
            .orElseThrow(() ->
                new PlayListNotFoundException(playListId,
                    "At some stage we were not able to find one of the request-related playlists."));
    }

    @Override
    public void deletePlayList(String accessToken, String playListId) throws IOException {
        youTubeClient
            .playlists()
            .delete(playListId)
            .setAccessToken(accessToken)
            .execute();
    }

    @Override
    public String exportPlaylist(String accessToken, String playlistId) throws IOException {
        var videosInPlaylist = videoService.getPlaylistVideosPagination(accessToken, playlistId);
        var videoDtoList = videosInPlaylist.stream().map(PlaylistItemMapper::mapToDto).toList();
        var filePath = "./playlist_export.csv";
        CsvWriter.writePlaylistVideosToCsv(videoDtoList, filePath);

        return filePath;
    }
}
