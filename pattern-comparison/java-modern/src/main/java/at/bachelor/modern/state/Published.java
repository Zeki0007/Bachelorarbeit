package at.bachelor.modern.state;

import java.time.LocalDateTime;

public record Published(String content, String reviewerName, LocalDateTime publishDate) implements DocumentState{
}
