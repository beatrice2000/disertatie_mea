package com.beatrice.book.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
}


