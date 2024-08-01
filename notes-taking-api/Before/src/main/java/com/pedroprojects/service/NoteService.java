package com.pedroprojects.service;

import com.pedroprojects.model.Note;
import com.pedroprojects.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note createNote(Note note) {
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public Note updateNote(Long id, Note update) {
        Note note = getNoteById(id);
        if (note == null) {
            return null;
        }
        if (update.getTitle() != null) {
            note.setTitle(update.getTitle());
        }
        if (update.getBody() != null) {
            note.setBody(update.getBody());
        }
        if (update.getCreatedBy() != null) {
            Note.EditHistory editHistory = new Note.EditHistory();
            editHistory.setEditedBy(update.getCreatedBy());
            editHistory.setEditedAt(LocalDateTime.now());
            note.getEditHistory().add(editHistory);
        }
        return noteRepository.save(note);
    }

    public boolean deleteNoteById(Long id) {
        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
