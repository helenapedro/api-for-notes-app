package com.pedroprojects.controller;

import com.pedroprojects.model.Note;
import com.pedroprojects.model.User;
import com.pedroprojects.service.NoteService;
import com.pedroprojects.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<Note>> getNotesByDateRange(
            Authentication authentication,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Note> notes = noteService.getNotesByUserAndDateRange(user.getUid(), startDate, endDate, pageable);
        return ResponseEntity.ok(notes);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> createNote(Authentication authentication, @RequestBody Note note) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Note createdNote = noteService.createNote(note, user);
        return ResponseEntity.ok(createdNote);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note update) {
        Note note = noteService.updateNote(id, update);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(note);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        if (noteService.deleteNoteById(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
