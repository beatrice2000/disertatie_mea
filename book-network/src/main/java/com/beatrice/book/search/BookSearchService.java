package com.beatrice.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final ElasticsearchOperations elasticsearchTemplate;

    public List<BookDocument> fuzzySearch(String query) {
        Criteria criteria = new Criteria("title").matches(query)
                .or(new Criteria("authorName").matches(query))
                .or(new Criteria("resume").matches(query));

        CriteriaQuery searchQuery = new CriteriaQuery(criteria);
        return elasticsearchTemplate
                .search(searchQuery, BookDocument.class)
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }
}
