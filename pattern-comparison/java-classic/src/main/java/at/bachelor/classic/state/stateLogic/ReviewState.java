package at.bachelor.classic.state.stateLogic;

import java.time.LocalDateTime;

public class ReviewState implements DocumentState {
    @Override
    public void edit(Document document, String newContent) {
        throw new IllegalStateException("A document under review must not be edited!");
    }

    @Override
    public void submitForReview(Document document) {
        throw new IllegalStateException("The document is already under review!");
    }

    @Override
    public void approve(Document document, String reviewerName,LocalDateTime publishDate) {
        document.setReviewerName(reviewerName);
        document.setPublishDate(publishDate);
        document.setState(new PublishedState());
    }

    @Override
    public void reject(Document document) {
        document.setState(new DraftState());
    }
}
