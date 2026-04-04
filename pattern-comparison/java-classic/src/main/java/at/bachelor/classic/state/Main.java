package at.bachelor.classic.state;

import at.bachelor.classic.state.stateLogic.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Document doc = new Document("My first bachelor's thesis");
        System.out.println("Start: " + doc.getStateName() + " | Content: " + doc.getContent());

        doc.edit("My final bachelor's thesis");
        System.out.println("After Edit: " + doc.getStateName() + " | Content: " + doc.getContent());

        doc.submitForReview();
        System.out.println("Submitted: " + doc.getStateName());

        try {
            doc.edit("I want to change something!");
        } catch (IllegalStateException e) {
            System.out.println("Error caught: " + e.getMessage());
        }

        doc.approve("Prof. Dr. Schmidt", LocalDate.of(2026,1,1).atStartOfDay());

        System.out.println("Approve: " + doc.getStateName());
        System.out.println(" -> Reviewer: " + doc.getReviewerName());
        System.out.println(" -> PublishDate: " + doc.getPublishDate().toLocalDate());
        System.out.println(" -> Content: " + doc.getContent());

        Document newDoc = new Document("Second text");
        System.out.println("\nDANGER: The reviewer from the new doctor is: " + newDoc.getReviewerName()); // will return null
    }
}