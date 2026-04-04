package at.bachelor.modern.state;

public class Main {
    public static void main(String[] args) {
        DocumentState doc = new Draft("My final bachelor's thesis");
        System.out.println("Start: " + doc);

        doc = WorkflowService.edit(doc, "My final bachelor's thesis Edited");
        System.out.println("After Edit: " + doc);

        doc = WorkflowService.submitForReview(doc);
        System.out.println("Submitted: " + doc);

        try {
            doc = WorkflowService.edit(doc, "I want to change something!");
        } catch (IllegalStateException e) {
            System.out.println("Error caught: " + e.getMessage());
        }

        doc = WorkflowService.approve(doc, "Prof. Dr. Schmidt", java.time.LocalDate.of(2026,1,1).atStartOfDay());
        System.out.println("Approve: " + doc);
        if (doc instanceof Published(String content, String reviewerName, java.time.LocalDateTime publishDate)) {
            System.out.println(" -> Reviewer: " + reviewerName);
            System.out.println(" -> PublishDate: " + publishDate.toLocalDate());
            System.out.println(" -> Content: " + content);
        }

        Draft newDraft = new Draft("Second text");
    }
}