package com.beatrice.book.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
}


