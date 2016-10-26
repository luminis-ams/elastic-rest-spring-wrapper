package eu.luminis.elastic.cluster.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClusterHealth {
    @JsonProperty(value = "cluster_name")
    private String clusterName;
    @JsonProperty(value = "status")
    private String status;
    @JsonProperty(value = "number_of_nodes")
    private int numberOfNodes;
    private Object numberOfDataNodes;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClusterHealth{" +
                "clusterName='" + clusterName + '\'' +
                ", status='" + status + '\'' +
                ", numberOfNodes=" + numberOfNodes +
                '}';
    }

}
