package eu.luminis.elastic;

import eu.luminis.elastic.cluster.ClusterService;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import eu.luminis.elastic.monitoring.ElasticHealthIndicator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static eu.luminis.elastic.SingleClusterRestClientFactoryBean.DEFAULT_CLUSTER_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ElasticHealthIndicatorTest {

    @Mock
    ClusterService clusterService;

    private ElasticHealthIndicator indicator;

    @Before
    public void setUp() throws Exception {
        this.indicator = new ElasticHealthIndicator(clusterService);
    }

    @Test
    public void testHealthCheckGreen() {
        ClusterHealth clusterHealth = createClusterHealth("green");
        when(clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME)).thenReturn(clusterHealth);

        Health health = indicator.health();
        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    public void testHealthCheckYellow() {
        ClusterHealth clusterHealth = createClusterHealth("yellow");
        when(clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME)).thenReturn(clusterHealth);

        Health health = indicator.health();
        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    public void testHealthCheckRed() {
        ClusterHealth clusterHealth = createClusterHealth("red");
        when(clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME)).thenReturn(clusterHealth);

        Health health = indicator.health();
        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    public void testHealthCheckOther() {
        ClusterHealth clusterHealth = createClusterHealth("other");
        when(clusterService.checkClusterHealth(DEFAULT_CLUSTER_NAME)).thenReturn(clusterHealth);

        Health health = indicator.health();
        assertEquals(Status.DOWN, health.getStatus());
    }

    private ClusterHealth createClusterHealth(String status) {
        ClusterHealth clusterHealth = new ClusterHealth();
        clusterHealth.setStatus(status);
        clusterHealth.setClusterName("mockCluster");
        clusterHealth.setNumberOfNodes(1);
        return clusterHealth;
    }
}