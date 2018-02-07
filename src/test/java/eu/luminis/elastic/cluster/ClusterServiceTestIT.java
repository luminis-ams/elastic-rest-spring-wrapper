package eu.luminis.elastic.cluster;

import eu.luminis.elastic.RestClientConfig;
import eu.luminis.elastic.TestConfig;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import eu.luminis.elastic.index.IndexService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestClientConfig.class, TestConfig.class})
public class ClusterServiceTestIT {

    public static final String CLUSTER_NAME = "my-local-cluster";
    @Autowired
    private ClusterService clusterService;

    @Autowired
    private IndexService indexService;

    @Before
    public void setUp() throws Exception {
        clusterService
                .addCluster(CLUSTER_NAME, Collections.singletonList("localhost:19200"), false);
    }

    @Test
    public void checkClusterHealth() {
        if (!indexService.indexExist(CLUSTER_NAME, "cluster_test")) {
            indexService.createIndex(CLUSTER_NAME, "cluster_test", "{}");
        }

        ClusterHealth clusterHealth = clusterService.checkClusterHealth(CLUSTER_NAME);
        assertEquals("test",clusterHealth.getClusterName());
        assertEquals(1,clusterHealth.getNumberOfNodes());
        assertEquals("yellow", clusterHealth.getStatus());
        assertTrue(clusterHealth.getActivePrimaryShards()>= 5);
        assertTrue(clusterHealth.getUnassignedShards() >= 5);
        assertTrue(clusterHealth.getActiveShards() >= 5);
        assertEquals(0,clusterHealth.getRelocatingShards());
        assertEquals(0,clusterHealth.getInitializingShards());
        assertEquals(0,clusterHealth.getDelayedUnassignedShards());
        assertFalse(clusterHealth.getTimedOut());
        assertEquals(1, clusterHealth.getNumberOfDataNodes());
        assertEquals(1, clusterHealth.getNumberOfDataNodes());
        assertEquals(0,clusterHealth.getNumberOfPendingTasks());
        assertEquals(0,clusterHealth.getNumberOfInFlightFetch());
        assertEquals(0,clusterHealth.getTaskMaxWaitingInQueueMillis());
        assertNotNull(clusterHealth.toString());
    }

    @Test
    public void testRemovingCluster() {
        clusterService.deleteCluster(CLUSTER_NAME);

        try {
            clusterService.checkClusterHealth(CLUSTER_NAME);
            fail("CLusterApiException should have been thrown");
        } catch (ClusterApiException e) {
            assertEquals("The provided cluster name does not result in a valid cluster", e.getMessage());
        }
    }

}