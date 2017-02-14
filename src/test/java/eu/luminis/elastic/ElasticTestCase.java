package eu.luminis.elastic;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.node.internal.InternalSettingsPreparer;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public abstract class ElasticTestCase {
    private static Node node = null;

    @BeforeClass
    public static void setupOnce() throws NodeValidationException {
        Settings settings = Settings.builder()
                .put("path.home", "target/elasticsearch")
                .put("transport.type", "local")
                .put("http.enabled", true)
                .build();

        Collection plugins = Collections.singletonList(Netty4Plugin.class);
        node = new PluginConfigurableNode(settings, plugins).start();
    }

    @AfterClass
    public static void teardownOnce() throws IOException {
        node.close();
    }

    private static class PluginConfigurableNode extends Node {
        PluginConfigurableNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {
            super(InternalSettingsPreparer.prepareEnvironment(settings, null), classpathPlugins);
        }
    }
}
