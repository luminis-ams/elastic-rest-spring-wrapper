package eu.luminis.elastic.cluster;

import eu.luminis.elastic.cluster.response.ClusterHealth;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleClusterClusterServiceTest {

    @Mock
    ClusterService clusterService;

    SingleClusterClusterService singleClusterClusterService;

    @Before
    public void setUp() throws Exception {
        singleClusterClusterService = new SingleClusterClusterService(clusterService);
    }

    @Test
    public void testCheckClusterHealth() {
        when(clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME)).thenReturn(createClusterHealth());

        ClusterHealth clusterHealth = singleClusterClusterService.checkClusterHealth();

        assertEquals("green", clusterHealth.getStatus());
    }

    private ClusterHealth createClusterHealth() {
        ClusterHealth clusterHealth = new ClusterHealth();
        clusterHealth.setStatus("green");
        clusterHealth.setClusterName(DEFAULT_CLUSTER_NAME);
        clusterHealth.setNumberOfNodes(1);
        return clusterHealth;
    }

}