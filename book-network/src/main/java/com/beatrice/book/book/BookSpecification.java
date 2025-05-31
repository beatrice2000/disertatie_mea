package com.beatrice.book.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    // creez o metoda statica Specification de tip Book pe care o numesc withOwnerId
    public static Specification<Book> withOwnerId(String ownerId) {
        // ce avem nevoie pentru Specification
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy"), ownerId);
    }
}
