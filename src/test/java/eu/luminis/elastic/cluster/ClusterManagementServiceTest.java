package eu.luminis.elastic.cluster;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import eu.luminis.elastic.LoggingFailureListener;

@RunWith(MockitoJUnitRunner.class)
public class ClusterManagementServiceTest {

    private static final String CLUSTER_NAME = "default-test-cluster";
    private static final String NON_EXISTENT_CLUSTER_NAME = "non-existent-cluster";
    private static final List<String> HOSTS = Collections.singletonList("localhost:9200");

    @InjectMocks
    private ClusterManagementService clusterManagementService = new ClusterManagementService();

    @Test
    public void testAddCluster() {
        assertThat(clusterManagementService.addCluster(CLUSTER_NAME, HOSTS, false)).isNotNull();
        assertThat(clusterManagementService.addCluster(CLUSTER_NAME, HOSTS, false)).isNull();
    }

    @Test
    public void testDeleteCluster() {
        clusterManagementService.addCluster(CLUSTER_NAME, HOSTS,false );

        assertThat(clusterManagementService.deleteCluster(NON_EXISTENT_CLUSTER_NAME)).isNull();
        assertThat(clusterManagementService.deleteCluster(CLUSTER_NAME)).isNotNull();
    }
}
