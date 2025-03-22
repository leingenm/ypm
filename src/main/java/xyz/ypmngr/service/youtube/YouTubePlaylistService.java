package xyz.ypmngr.service.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.ypmngr.dto.mapper.PlaylistMapper;
import xyz.ypmngr.dto.mapper.PlaylistVideoMapper;
import xyz.ypmngr.enumeration.Part;
import xyz.ypmngr.exception.GoogleRespondedErrorException;
import xyz.ypmngr.exception.NotFoundException;
import xyz.ypmngr.model.Playlist;
import xyz.ypmngr.model.Video;
import xyz.ypmngr.service.PlaylistService;
import xyz.ypmngr.service.auth.AuthService;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouTubePlaylistService implements PlaylistService {

    private final YouTube youTubeClient;
    private final AuthService authService;

    @Override
    public List<Playlist> getAllUserPlaylists() {
        log.info("Retrieving all users playlists");
        try {
            var ytPlaylists = youTubeClient
                    .playlists()
                    .list(List.of(Part.SNIPPET.toString(), Part.STATUS.toString()))
                    .setAccessToken(authService.getToken())
                    .setMine(true)
                    .execute()
                    .getItems();

            return ytPlaylists.stream().map(ytPlaylist -> {
                var ypmPlaylist = PlaylistMapper.map(ytPlaylist);
                ypmPlaylist.setVideos(getVideos(null, ypmPlaylist.getId()));
                return ypmPlaylist;
            }).toList();
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public Playlist getPlaylistById(String playListId) {
        try {
            var ytPlaylist = youTubeClient
                    .playlists()
                    .list(List.of(Part.SNIPPET.toString(), Part.STATUS.toString()))
                    .setId(List.of(playListId))
                    .setAccessToken(authService.getToken())
                    .execute()
                    .getItems()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(
                            playListId,
                            "We were not able to find one of the request-related playlists.")
                    );

            var ypmPlaylist = PlaylistMapper.map(ytPlaylist);
            ypmPlaylist.setVideos(getVideos(null, ypmPlaylist.getId()));
            return ypmPlaylist;
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        log.info("Attempting to create playlist: {}", playlist);
        var ytPlaylist = new com.google.api.services.youtube.model.Playlist();
        ytPlaylist
                .setStatus(new PlaylistStatus().setPrivacyStatus(requireNonNull(playlist.getStatus()).getValue()))
                .setSnippet(new PlaylistSnippet().setTitle(playlist.getTitle()).setDescription(playlist.getDescription()));
        try {
            var createdYtPlaylist = youTubeClient
                    .playlists()
                    .insert(List.of(Part.STATUS.toString(), Part.SNIPPET.toString()), ytPlaylist)
                    .setAccessToken(authService.getToken())
                    .execute();

            log.info("Playlist created, id = {}", createdYtPlaylist.getId());
            return PlaylistMapper.map(createdYtPlaylist);
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public Playlist updatePlaylistTitle(String playListId, String newTitle) {
        var playlistToEdit = getPlaylistById(playListId);
        var ytPlaylistForRequest = new com.google.api.services.youtube.model.Playlist();
        ytPlaylistForRequest
                .setId(playlistToEdit.getId())
                .setSnippet(new PlaylistSnippet())
                .getSnippet()
                .setTitle(newTitle);

        try {
            var updatedYtPlaylist = youTubeClient
                    .playlists()
                    .update(List.of(Part.SNIPPET.toString(), Part.STATUS.toString()), ytPlaylistForRequest)
                    .setAccessToken(authService.getToken())
                    .execute();

            return PlaylistMapper.map(updatedYtPlaylist);
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public void deletePlaylist(String playListId) {
        try {
            youTubeClient
                    .playlists()
                    .delete(playListId)
                    .setAccessToken(authService.getToken())
                    .execute();
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public void removeVideoFromPlaylist(String videoId) {
        try {
            youTubeClient
                    .playlistItems()
                    .delete(videoId)
                    .setAccessToken(authService.getToken())
                    .execute();
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    @Override
    public Playlist mergePlaylists(
            Playlist playlist,
            List<String> playListsIds,
            boolean deleteAfterMerge
    ) {
        var resultingPlaylist = createPlaylist(playlist);
        for (var playListId : playListsIds) {
            var videos = getVideos(null, playListId);
            var videosIds = videos.stream().map(Video::getPlaylistItemId).toList();
            moveVideosBetweenPlaylists(playListId, resultingPlaylist.getId(), videosIds, deleteAfterMerge);
            if (deleteAfterMerge) deletePlaylist(playListId);
        }
        return getPlaylistById(resultingPlaylist.getId());
    }

    @Override
    public Playlist moveVideosBetweenPlaylists(
            String playListId,
            String targetPlayListId,
            List<String> videosIds,
            boolean deleteAfterMove
    ) {
        var videosToMove = getVideos(videosIds, null);
        for (Video video : videosToMove) {
            if (requireNonNull(video.getPlaylistId()).equals(playListId)) {
                insertVideoIntoPlaylist(video.getGlobalId(), targetPlayListId);
                if (deleteAfterMove) removeVideoFromPlaylist(video.getPlaylistItemId());
            }
        }
        return getPlaylistById(targetPlayListId);
    }

    @Override
    public void insertVideoIntoPlaylist(String videoGlobalId, String playlistId) {
        var playlistVideoToInsert = new PlaylistItem();
        playlistVideoToInsert
                .setKind("youtube#playlistItem")
                .setSnippet(
                        new PlaylistItemSnippet()
                                .setPlaylistId(playlistId)
                                .setResourceId(
                                        new ResourceId()
                                                .setVideoId(videoGlobalId)
                                                .setKind("youtube#video")));
        try {
            youTubeClient
                    .playlistItems()
                    .insert(List.of(Part.SNIPPET.toString()), playlistVideoToInsert)
                    .setAccessToken(authService.getToken())
                    .execute();
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }

    private List<Video> getVideos(List<String> videosIds, String playlistId) {
        try {
            var ytPlaylistVideos = youTubeClient
                    .playlistItems()
                    .list(List.of(Part.SNIPPET.toString(), Part.CONTENT_DETAILS.toString()))
                    .setId(videosIds)
                    .setPlaylistId(playlistId)
                    .setAccessToken(authService.getToken())
                    .execute()
                    .getItems();
            return PlaylistVideoMapper.mapAll(ytPlaylistVideos);
        } catch (IOException e) {
            throw new GoogleRespondedErrorException(e);
        }
    }
}
