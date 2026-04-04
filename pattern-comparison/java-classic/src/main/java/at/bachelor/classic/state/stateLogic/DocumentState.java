package at.bachelor.classic.state.stateLogic;

import java.time.LocalDateTime;

public interface DocumentState {
    void edit(Document document, String newContent);
    void submitForReview(Document document);
    void approve(Document document, String reviewerName, LocalDateTime publishDate);
    void reject(Document document);
}
