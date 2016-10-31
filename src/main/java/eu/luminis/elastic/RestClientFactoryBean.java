package eu.luminis.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.sniff.Sniffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Factory bean for creating the RestClient instance(s)
 */
@Component
public class RestClientFactoryBean extends AbstractFactoryBean<RestClient> {
    private static final Logger logger = LoggerFactory.getLogger(RestClientFactoryBean.class);

    private final LoggingFailureListener loggingFailureListener;

    private String[] hostnames;

    private Sniffer sniffer;

    @Autowired
    public RestClientFactoryBean(LoggingFailureListener loggingFailureListener) {
        this.loggingFailureListener = loggingFailureListener;
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    protected RestClient createInstance() throws Exception {
        HttpHost[] hosts = new HttpHost[hostnames.length];
        for (int i = 0; i < hosts.length; i++) {
            hosts[i] = HttpHost.create(hostnames[i]);
        }
        RestClient restClient = RestClient
                .builder(hosts)
                .setFailureListener(loggingFailureListener)
                .build();

        this.sniffer = Sniffer.builder(restClient).build();

        return restClient;
    }

    @Override
    protected void destroyInstance(RestClient instance) throws Exception {
        try {
            logger.info("Closing the elasticsearch sniffer");
            instance.close();
            this.sniffer.close();
        } catch (IOException e) {
            logger.warn("Failed to close the elasticsearch sniffer");
        }
    }

    @Value("${eu.luminis.elastic.hostnames:#{\"localhost:9200\"}}")
    public void setHostnames(String[] hostnames) {
        this.hostnames = hostnames;
    }
}
