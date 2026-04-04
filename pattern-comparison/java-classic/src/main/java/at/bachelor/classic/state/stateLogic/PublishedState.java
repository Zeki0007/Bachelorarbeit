package at.bachelor.classic.state.stateLogic;

import java.time.LocalDateTime;

public class PublishedState implements DocumentState {
    @Override
    public void edit(Document document, String newContent) {
        throw new IllegalStateException("A published document is write-protected!");
    }

    @Override
    public void submitForReview(Document document) {
        throw new IllegalStateException("Already published!");
    }

    @Override
    public void approve(Document document, String reviewerName, LocalDateTime publishDate) {
        throw new IllegalStateException("Already published!");
    }

    @Override
    public void reject(Document document) {
        throw new IllegalStateException("Once published, it can no longer be rejected!");
    }
}