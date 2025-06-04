package com.beatrice.book.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

@Autowired(required = false)
public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, String> {
}


