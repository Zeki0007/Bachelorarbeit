package at.bachelor.classic.state.stateLogic;

import java.time.LocalDateTime;

public class Document {
    private DocumentState state;
    private String content;
    private String reviewerName; // can be null
    private LocalDateTime publishDate; // can be null

    public Document(String initialContent) {
        this.content = initialContent;
        this.state = new DraftState(); // Starting state
    }

    // The actions are delegated to the state
    public void edit(String newContent) {
        state.edit(this, newContent);
    }
    public void submitForReview() {
        state.submitForReview(this);
    }
    public void approve(String reviewerName, LocalDateTime publishDate) {
        state.approve(this, reviewerName, publishDate);
    }
    public void reject() {
        state.reject(this);
    }

    // Setters, which are called by the states
    protected void setState(DocumentState state) {
        this.state = state;
    }
    protected void setContent(String content) {
        this.content = content;
    }
    protected void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    protected void setPublishDate(LocalDateTime publishDate) {
        this.publishDate = publishDate;
    }

    //Getter
    public String getContent() {
        return content;
    }
    public String getReviewerName() {
        return reviewerName;
    }
    public LocalDateTime getPublishDate() {
        return publishDate;
    }
    public String getStateName() {
        return state.getClass().getSimpleName();
    }
}