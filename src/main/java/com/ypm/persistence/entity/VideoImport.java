package com.ypm.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
public class VideoImport {

    @Id
    private String videoId;

    @Column(name = "creation_timestamp")
    private OffsetDateTime dateAdded;

    public VideoImport(String videoId, String videoTimeStamp) {
        this.videoId = videoId;
        this.dateAdded = OffsetDateTime.parse(videoTimeStamp); // TODO: Is it okay solution?
    }
}
