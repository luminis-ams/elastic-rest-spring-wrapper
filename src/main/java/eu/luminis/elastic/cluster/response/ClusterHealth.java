package eu.luminis.elastic.cluster.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterHealth {
    @JsonProperty(value = "cluster_name")
    private String clusterName;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "number_of_nodes")
    private int numberOfNodes;

    @JsonProperty(value = "number_of_data_nodes")
    private Object numberOfDataNodes;

    @JsonProperty(value = "timed_out")
    private Boolean timedOut;

    @JsonProperty(value = "active_primary_shards")
    private int activePrimaryShards;

    @JsonProperty(value = "active_shards")
    private int activeShards;

    @JsonProperty(value = "relocating_shards")
    private int relocatingShards;

    @JsonProperty(value = "initializing_shards")
    private int initializingShards;

    @JsonProperty(value = "unassigned_shards")
    private int unassignedShards;

    @JsonProperty(value = "delayed_unassigned_shards")
    private int delayedUnassignedShards;

    @JsonProperty(value = "number_of_pending_tasks")
    private int numberOfPendingTasks;

    @JsonProperty(value = "number_of_in_flight_fetch")
    private int numberOfInFlightFetch;

    @JsonProperty(value = "task_max_waiting_in_queue_millis")
    private int taskMaxWaitingInQueueMillis;


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

    public Object getNumberOfDataNodes() {
        return numberOfDataNodes;
    }

    public void setNumberOfDataNodes(Object numberOfDataNodes) {
        this.numberOfDataNodes = numberOfDataNodes;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    public int getActivePrimaryShards() {
        return activePrimaryShards;
    }

    public void setActivePrimaryShards(int activePrimaryShards) {
        this.activePrimaryShards = activePrimaryShards;
    }

    public int getActiveShards() {
        return activeShards;
    }

    public void setActiveShards(int activeShards) {
        this.activeShards = activeShards;
    }

    public int getRelocatingShards() {
        return relocatingShards;
    }

    public void setRelocatingShards(int relocatingShards) {
        this.relocatingShards = relocatingShards;
    }

    public int getInitializingShards() {
        return initializingShards;
    }

    public void setInitializingShards(int initializingShards) {
        this.initializingShards = initializingShards;
    }

    public int getUnassignedShards() {
        return unassignedShards;
    }

    public void setUnassignedShards(int unassignedShards) {
        this.unassignedShards = unassignedShards;
    }

    public int getDelayedUnassignedShards() {
        return delayedUnassignedShards;
    }

    public void setDelayedUnassignedShards(int delayedUnassignedShards) {
        this.delayedUnassignedShards = delayedUnassignedShards;
    }

    public int getNumberOfPendingTasks() {
        return numberOfPendingTasks;
    }

    public void setNumberOfPendingTasks(int numberOfPendingTasks) {
        this.numberOfPendingTasks = numberOfPendingTasks;
    }

    public int getNumberOfInFlightFetch() {
        return numberOfInFlightFetch;
    }

    public void setNumberOfInFlightFetch(int numberOfInFlightFetch) {
        this.numberOfInFlightFetch = numberOfInFlightFetch;
    }

    public int getTaskMaxWaitingInQueueMillis() {
        return taskMaxWaitingInQueueMillis;
    }

    public void setTaskMaxWaitingInQueueMillis(int taskMaxWaitingInQueueMillis) {
        this.taskMaxWaitingInQueueMillis = taskMaxWaitingInQueueMillis;
    }


    @Override
    public String toString() {
        return "ClusterHealth{" +
                "clusterName='" + clusterName + '\'' +
                ", status='" + status + '\'' +
                ", numberOfNodes=" + numberOfNodes +
                ", numberOfDataNodes=" + numberOfDataNodes +
                ", timedOut=" + timedOut +
                ", activePrimaryShards=" + activePrimaryShards +
                ", activeShards=" + activeShards +
                ", relocatingShards=" + relocatingShards +
                ", initializingShards=" + initializingShards +
                ", unassignedShards=" + unassignedShards +
                ", delayedUnassignedShards=" + delayedUnassignedShards +
                ", numberOfPendingTasks=" + numberOfPendingTasks +
                ", numberOfInFlightFetch=" + numberOfInFlightFetch +
                ", taskMaxWaitingInQueueMillis=" + taskMaxWaitingInQueueMillis +
                '}';
    }
}
