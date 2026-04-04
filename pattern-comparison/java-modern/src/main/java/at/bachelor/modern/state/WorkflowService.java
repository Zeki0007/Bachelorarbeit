package at.bachelor.modern.state;

import java.time.LocalDateTime;

public class WorkflowService {

    public static DocumentState edit(DocumentState currentState, String newContent) {
        return switch (currentState) {
            case Draft d -> new Draft(newContent);
            case Review r -> throw new IllegalStateException("A document under review must not be edited!");
            case Published p -> throw new IllegalStateException("A published document is write-protected!");
        };
    }

    public static DocumentState submitForReview(DocumentState currentState) {
        return switch (currentState) {
            case Draft d -> new Review(d.content());
            case Review r -> throw new IllegalStateException("The document is already under review!");
            case Published p -> throw new IllegalStateException("Already published!");
        };
    }

    public static DocumentState approve(DocumentState currentState, String reviewerName, LocalDateTime publishDate) {
        return switch (currentState) {
            case Draft d -> throw new IllegalStateException("A draft cannot be released directly!");
            case Review r -> new Published(r.content(), reviewerName, publishDate);
            case Published p -> throw new IllegalStateException("Already published!");
        };
    }

    public static DocumentState reject(DocumentState currentState) {
        return switch (currentState) {
            case Draft d -> throw new IllegalStateException("A draft cannot be rejected!");
            case Review r -> new Draft(r.content());
            case Published p -> throw new IllegalStateException("Once published, it can no longer be rejected!");
        };
    }
}