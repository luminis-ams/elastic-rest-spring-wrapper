package eu.luminis.elastic.monitoring;

import eu.luminis.elastic.cluster.ClusterService;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticHealthIndicator extends AbstractHealthIndicator {

    private final ClusterService clusterService;

    @Autowired
    public ElasticHealthIndicator(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        List<String> downClusters = clusterService.getAvailableClusters().stream().filter(cluster -> {
            ClusterHealth clusterHealth = clusterService.checkClusterHealth(cluster);

            return "red".equals(clusterHealth.getStatus());

        }). collect(Collectors.toList());

        if (downClusters.isEmpty()) {
            builder.up();
        } else {
            builder.down();
            builder.withDetail("downClusterNames", Arrays.toString(downClusters.toArray()));
        }

    }
}
