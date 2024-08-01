package com.pedroprojects.repository;

import com.pedroprojects.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;


public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser_Uid(Long userId);
    List<Note> findByUserUidAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    Page<Note> findByUser_Uid(Long userId, Pageable pageable);
    Page<Note> findByUserUidAndCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);  
}
