package com.pedroprojects.model;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private String createdBy;
    private LocalDateTime createdAt;

    @ElementCollection
    @CollectionTable(name = "edit_history", joinColumns = @JoinColumn(name = "note_id"))
    private List<EditHistory> editHistory = new ArrayList<>();

    @Embeddable
    @Data
    public static class EditHistory {
        private String editedBy;
        private LocalDateTime editedAt;
    }
}
