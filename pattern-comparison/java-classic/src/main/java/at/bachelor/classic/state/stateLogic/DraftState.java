package at.bachelor.classic.state.stateLogic;

import java.time.LocalDateTime;

public class DraftState implements DocumentState {
    @Override
    public void edit(Document document, String newContent) {
        document.setContent(newContent);
    }

    @Override
    public void submitForReview(Document document) {
        document.setState(new ReviewState());
    }

    @Override
    public void approve(Document document, String reviewerName, LocalDateTime publishDate) {
        throw new IllegalStateException("A draft cannot be released directly!");
    }

    @Override
    public void reject(Document document) {
        throw new IllegalStateException("A draft cannot be rejected!");
    }
}