package com.pedroprojects.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nid;

    private String title;
    private String body;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "uid", nullable = false)
    @JsonBackReference // to handle the bidirectional relationship correctly.
    private User user;

    @ElementCollection
    @CollectionTable(name = "edit_history", joinColumns = @JoinColumn(name = "note_id"))
    @OrderColumn(name = "idx")
    private List<EditHistory> editHistory = new ArrayList<>();

    @Embeddable
    @Data
    public static class EditHistory {
        private String editedBy;
        private LocalDateTime editedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
    	updatedAt = LocalDateTime.now();
    }
}
