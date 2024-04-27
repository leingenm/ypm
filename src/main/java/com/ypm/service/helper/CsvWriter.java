package com.ypm.service.helper;

import com.ypm.dto.PlayListItemDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvWriter {

    public static void writePlaylistVideosToCsv(List<PlayListItemDto> playListItems, String filePath) {
        var csvFormat = CSVFormat.DEFAULT.builder()
                                     .setHeader("Title", "Video Owner Channel Title", "Video ID", "Video Thumbnail URL")
                                     .build();

        try (var printer = new CSVPrinter(new FileWriter(filePath), csvFormat)) {
            for (var video : playListItems) {
                printer.printRecord(video.videoTitle(), video.videoOwnerChannelTitle(), video.videoId(), video.videoThumbnail());
            }
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
}
