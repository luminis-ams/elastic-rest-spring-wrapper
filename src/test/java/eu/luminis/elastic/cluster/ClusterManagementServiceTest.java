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
import eu.luminis.elastic.cluster.ClusterManagementService;

@RunWith(MockitoJUnitRunner.class)
public class ClusterManagementServiceTest {

    private static final String CLUSTER_NAME = "default-test-cluster";
    private static final String NON_EXISTENT_CLUSTER_NAME = "non-existent-cluster";
    private static final List<String> HOSTS = Collections.singletonList("localhost:9200");

    @Mock
    private LoggingFailureListener loggingFailureListener;
    @InjectMocks
    private ClusterManagementService factory = new ClusterManagementService(loggingFailureListener);

    @Test
    public void testAddCluster() {
        assertThat(factory.addCluster(CLUSTER_NAME, HOSTS)).isNotNull();
        assertThat(factory.addCluster(CLUSTER_NAME, HOSTS)).isNull();
    }

    @Test
    public void testDeleteCluster() {
        factory.addCluster(CLUSTER_NAME, HOSTS);

        assertThat(factory.deleteCluster(NON_EXISTENT_CLUSTER_NAME)).isNull();
        assertThat(factory.deleteCluster(CLUSTER_NAME)).isNotNull();
    }

    @Test
    public void testGetCurrentCluster() {
        factory.addCluster(CLUSTER_NAME, HOSTS);

        assertThat(factory.getCurrentCluster().isPresent()).isFalse();
        factory.setCurrentCluster(CLUSTER_NAME);
        assertThat(factory.getCurrentCluster().isPresent()).isTrue();
    }

    @Test
    public void testSetCurrentCluster() {
        assertThat(factory.setCurrentCluster(CLUSTER_NAME)).isNull();
        factory.addCluster(CLUSTER_NAME, HOSTS);
        assertThat(factory.setCurrentCluster(CLUSTER_NAME)).isNotNull();
    }
}
