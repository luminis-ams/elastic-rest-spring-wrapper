package eu.luminis.elastic.cluster;

import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import eu.luminis.elastic.ElasticTestCase;
import eu.luminis.elastic.index.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class ClusterServiceTest extends ElasticTestCase {

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private IndexService indexService;

    @Test
    public void checkClusterHealth() throws Exception {
        if (!indexService.indexExist("cluster_test")) {
            indexService.createIndex("cluster_test", "{}");
        }

        ClusterHealth clusterHealth = clusterService.checkClusterHealth();
        assertEquals("elasticsearch",clusterHealth.getClusterName());
        assertEquals(1,clusterHealth.getNumberOfNodes());
        assertEquals("yellow", clusterHealth.getStatus());
        assertTrue(clusterHealth.getActivePrimaryShards()>= 5);
        assertTrue(clusterHealth.getUnassignedShards() >= 5);
        assertFalse(clusterHealth.getTimedOut());
    }

}