package com.beatrice.book.feedback;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    private Double note;
    private String comment;
    private boolean ownFeedback; // vreau sa evidentiez feedback-ul care e dat de mine(ca user)
}
