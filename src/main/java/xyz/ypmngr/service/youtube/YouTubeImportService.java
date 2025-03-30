package xyz.ypmngr.service.youtube;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.ypmngr.exception.BadRequestException;
import xyz.ypmngr.model.Playlist;
import xyz.ypmngr.model.PrivacyStatus;
import xyz.ypmngr.service.ImportService;
import xyz.ypmngr.service.PlaylistService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class YouTubeImportService implements ImportService {

    private final PlaylistService playlistService;

    @Override
    public Playlist importFromCsv(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).contains("csv")) throw new BadRequestException("Endpoint allows csv files only.");
        var parsedVideos = this.parseCsv(file);
        var playlistTitle = String.format("YPM Playlist import from %s on %s UTC", file.getOriginalFilename(), Instant.now());
        var createdPlaylist = playlistService.createPlaylist(new Playlist().title(playlistTitle).privacyStatus(PrivacyStatus.PRIVATE));
        parsedVideos.forEach(videoGlobalId -> playlistService.insertVideoIntoPlaylist(videoGlobalId, createdPlaylist.getId()));
        return playlistService.getPlaylistById(createdPlaylist.getId());
    }

    private List<String> parseCsv(MultipartFile file) {
        var videosGlobalIds = new ArrayList<String>();
        try (var inputReader = new InputStreamReader(file.getInputStream())) {
            var format = CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader().get();
            var records = format.parse(inputReader);
            records.stream().iterator().forEachRemaining(record -> videosGlobalIds.add(record.get("Video ID")));
        } catch (IOException e) {
            throw new BadRequestException("Error parsing attached csv. Check if it's formatted according to RFC4180.", e);
        }
        return videosGlobalIds;
    }
}
