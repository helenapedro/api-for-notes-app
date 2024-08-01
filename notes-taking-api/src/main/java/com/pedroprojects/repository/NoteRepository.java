package com.pedroprojects.repository;

import com.pedroprojects.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser_Uid(Long userId);

    List<Note> findByUserUidAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
