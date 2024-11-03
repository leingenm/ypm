package com.ypm.persistence.repository;

import com.ypm.persistence.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByNameContainingIgnoreCase(String playlistName);
}
