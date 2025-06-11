package com.beatrice.book;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootTest(classes = BookNetworkApiApplication.class)
@Disabled("Disabled until Elasticsearch is available in CI")
class BookNetworkApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
