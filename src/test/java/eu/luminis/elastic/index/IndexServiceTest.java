package eu.luminis.elastic.index;

import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.RestClientConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class IndexServiceTest extends ElasticTestCase {

    @Autowired
    private IndexService indexService;

    @Test
    public void createIndex() throws Exception {
        Boolean test_create = indexService.indexExist("test_create");
        if (test_create) {
            indexService.dropIndex("test_create");
        }
        indexService.createIndex("test_create", "{}");
        test_create = indexService.indexExist("test_create");
        assertTrue("The index should now exist", test_create);
    }

    @Test
    public void refreshIndexes() throws Exception {
        if (!indexService.indexExist("test_refresh")) {
            indexService.createIndex("test_refresh", "{}");
        }
        if (!indexService.indexExist("test_refresh2")) {
            indexService.createIndex("test_refresh2", "{}");
        }
        indexService.refreshIndexes();
        indexService.refreshIndexes("test_refresh");
        indexService.refreshIndexes("test_refresh", "test_refresh2");
    }

}