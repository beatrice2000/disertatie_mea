package com.beatrice.book.feedback;

import com.beatrice.book.book.Book;
import com.beatrice.book.book.BookRepository;
import com.beatrice.book.common.PageResponse;
import com.beatrice.book.exception.OperationNotPermittedException;
import com.beatrice.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedbackRequest request, Authentication connectedUser) {
        // prima data trebuie sa obtinem cartea dorita
        Book book = bookRepository.findById(request.bookId())
                // arunc exceptie daca cartea nu e gasita
                .orElseThrow(() -> new EntityNotFoundException("Book with the ID:: " + request.bookId() + " not found."));
        if (book.isArchived() || !book.isSharable()) {
            throw new OperationNotPermittedException("The feedback cannot be done because the book has been archived or is not available for sharing.");
        }

        if(Objects.equals(book.getCreatedBy(), connectedUser.getName())) {
            // returnez execeptie daca ownerul cartii e acelasi ca userul conectat pentru ca nu ar trebui sa imi dau singur feedback
            throw new OperationNotPermittedException("You are not allowed to give a feedback to your own book.");
        }
        Feedback feedback = feedbackMapper.toFeedback(request);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
