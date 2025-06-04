package com.beatrice.book.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final ElasticsearchOperations elasticsearchTemplate;

    public List<BookDocument> fuzzySearch(String query) {
        Criteria exactCriteria = new Criteria("title").is(query);
        CriteriaQuery exactQuery = new CriteriaQuery(exactCriteria);

        SearchHits<BookDocument> exactHits = elasticsearchTemplate.search(exactQuery, BookDocument.class);
        List<BookDocument> results = exactHits.stream()
                .map(SearchHit::getContent)
                .toList();

        // Dacă nu s-a găsit nimic, fallback cu fuzzy
        if (results.isEmpty()) {
            Criteria fuzzyCriteria = new Criteria("title").fuzzy(query);
            CriteriaQuery fuzzyQuery = new CriteriaQuery(fuzzyCriteria);

            SearchHits<BookDocument> fuzzyHits = elasticsearchTemplate.search(fuzzyQuery, BookDocument.class);
            results = fuzzyHits.stream()
                    .map(SearchHit::getContent)
                    .toList();
        }
        return results;
    }
}
