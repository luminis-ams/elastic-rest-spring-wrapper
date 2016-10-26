package eu.luminis.elastic;

import eu.luminis.elastic.cluster.ClusterService;
import eu.luminis.elastic.cluster.response.ClusterHealth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
public class ElasticHealthIndicator extends AbstractHealthIndicator {

    private final ClusterService clusterService;

    @Autowired
    public ElasticHealthIndicator(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        ClusterHealth clusterHealth = clusterService.checkClusterHealth();

        switch (clusterHealth.getStatus()) {
            case "green":
            case "yellow":
                builder.up();
                break;
            case "red":
            default:
                builder.down();
                break;
        }
        builder.withDetail("clusterName", clusterHealth.getClusterName());
        builder.withDetail("numberOfNodes", clusterHealth.getNumberOfNodes());
    }
}
