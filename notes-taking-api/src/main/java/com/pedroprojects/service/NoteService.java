package com.pedroprojects.service;

import com.pedroprojects.exception.UnauthorizedException;
import com.pedroprojects.model.Note;
import com.pedroprojects.model.User;
import com.pedroprojects.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @PreAuthorize("isAuthenticated()")
    public Page<Note> getNotesByUserAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        if (startDate == null && endDate == null) {
            return noteRepository.findByUser_Uid(userId, pageable);
        }
        if (startDate == null) {
            startDate = LocalDateTime.MIN; // Epoch start
        }
        if (endDate == null) {
            endDate = LocalDateTime.now(); // Current date
        }
        return noteRepository.findByUserUidAndCreatedAtBetween(userId, startDate, endDate, pageable);
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    @PreAuthorize("isAuthenticated()")
    public Note createNote(Note note, User user) {
        note.setUser(user);
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note update, User user) {
        Note note = getNoteById(id);
        if (note == null) {
            return null;
        }
        if (!note.getUser().getUid().equals(user.getUid())) {
            throw new UnauthorizedException("You are not authorized to update this note.");
        }
        if (update.getTitle() != null) {
            note.setTitle(update.getTitle());
        }
        if (update.getBody() != null) {
            note.setBody(update.getBody());
        }
        if (update.getCreatedBy() != null) {
            if (note.getEditHistory() == null) {
                note.setEditHistory(new ArrayList<>());
            }
            Note.EditHistory editHistory = new Note.EditHistory();
            editHistory.setEditedBy(update.getCreatedBy());
            editHistory.setEditedAt(LocalDateTime.now());
            note.getEditHistory().add(editHistory);
        }
        note.setUpdatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public boolean deleteNoteById(Long id, User user) {
        Note note = getNoteById(id);
        if (note == null) {
            return false;
        }
        if (!note.getUser().getUid().equals(user.getUid())) {
            throw new UnauthorizedException("You are not authorized to delete this note.");
        }
        noteRepository.deleteById(id);
        return true;
    }
}
