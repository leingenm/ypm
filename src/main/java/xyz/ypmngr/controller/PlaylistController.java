package xyz.ypmngr.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import xyz.ypmngr.api.PlaylistsApi;
import xyz.ypmngr.model.MergePlaylistsRequest;
import xyz.ypmngr.model.MoveVideosRequest;
import xyz.ypmngr.model.Playlist;
import xyz.ypmngr.service.PlaylistService;

@RestController
@RequiredArgsConstructor
public class PlaylistController implements PlaylistsApi {

    private final PlaylistService playlistService;

    @Override
    public ResponseEntity<List<Playlist>> getAllUserPlaylists() {
        return ResponseEntity.ok(playlistService.getAllUserPlaylists());
    }

    @Override
    public ResponseEntity<Playlist> getPlaylistById(String playlistId) {
        return ResponseEntity.ok(playlistService.getPlaylistById(playlistId));
    }

    @Override
    public ResponseEntity<Playlist> createPlaylist(Playlist playlist) {
        return ResponseEntity.ok(playlistService.createPlaylist(playlist));
    }

    @Override
    public ResponseEntity<Playlist> updatePlaylistTitle(String playlistId, Playlist playlist) {
        var newTitle = playlist.getTitle();
        var updatedPlaylist = playlistService.updatePlaylistTitle(playlistId, newTitle);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @Override
    public ResponseEntity<Void> deletePlaylist(String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> removeVideoFromPlaylist(String videoId) {
        playlistService.removeVideoFromPlaylist(videoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Playlist> mergePlaylists(MergePlaylistsRequest mergePlaylistsRequest) {
        var mergedPlaylist = playlistService
            .mergePlaylists(
                mergePlaylistsRequest.getResultingPlaylist(),
                mergePlaylistsRequest.getPlaylistsIds(),
                mergePlaylistsRequest.getDeleteAfterMerge()
            );
        return ResponseEntity.ok(mergedPlaylist);
    }

    @Override
    public ResponseEntity<Playlist> moveVideosBetweenPlaylists(
        MoveVideosRequest moveVideosRequest) {
        var playlistContainingMovedVideos = playlistService.moveVideosBetweenPlaylists(
            moveVideosRequest.getFromPlaylistId(),
            moveVideosRequest.getTargetPlaylistId(),
            moveVideosRequest.getVideosIds(),
            moveVideosRequest.getDeleteAfterMove()
        );
        return ResponseEntity.ok(playlistContainingMovedVideos);
    }
}
