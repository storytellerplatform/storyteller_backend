package com.example.storyteller.repository;

import com.example.storyteller.entity.Audio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface AudioRepository extends JpaRepository<Audio, Integer> {

    List<Audio> findAllByOrderByAudioIdDesc(Pageable pageable);
}
