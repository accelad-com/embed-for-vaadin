/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bsb.common.vaadin.embed;

import com.bsb.common.vaadin.embed.component.EmbedComponentConfig;
import org.junit.Test;

import java.util.Properties;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author Stephane Nicoll
 */
public class EmbedVaadinServerTest extends AbstractEmbedTest {

    @Test
    public void getDeployUrlWithDefaultSettings() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(EmbedComponentConfig.defaultConfig());
        assertEquals("http://localhost:[auto]/", server.getConfig().getDeployUrl());
    }

    @Test
    public void getDeployUrlWithCustomHttpPort() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(createCustomConfig(8080, null));
        assertEquals("http://localhost:8080/", server.getConfig().getDeployUrl());
    }

    @Test
    public void getDeployUrlWithCustomHttpPortAndContextRoot() {
        final TestableEmbedVaadinServer server = new TestableEmbedVaadinServer(createCustomConfig(8080, "/foo"));
        assertEquals("http://localhost:8080/foo", server.getConfig().getDeployUrl());
    }

    @Test
    public void productionModeWithDefaultSettings() {
        final TestableEmbedVaadinServer server = createConfiguredServer(EmbedComponentConfig.defaultConfig());
        assertContextParameter(server, AbstractEmbedVaadinTomcat.PRODUCTION_MODE_PARAM, "false");
    }

    @Test
    public void productionModeEnabled() {
        final Properties properties = new Properties();
        properties.put(EmbedVaadinConfig.KEY_PRODUCTION_MODE, "true");
        final TestableEmbedVaadinServer server = createConfiguredServer(new EmbedVaadinConfig(properties));
        assertContextParameter(server, AbstractEmbedVaadinTomcat.PRODUCTION_MODE_PARAM, "true");
    }

    private void assertContextParameter(TestableEmbedVaadinServer server, String key, String expectedValue) {
        final String actual = server.getContext().findParameter(key);
        assertNotNull("Context parameter [" + key + "] should have been found", actual);
        assertEquals("Wrong value for context parameter [" + key + "]", expectedValue, actual);
    }

    private TestableEmbedVaadinServer createConfiguredServer(EmbedVaadinConfig config) {
        final TestableEmbedVaadinServer result = new TestableEmbedVaadinServer(config);
        result.configure();
        return result;
    }

    @SuppressWarnings("serial")
    private static final class TestableEmbedVaadinServer extends AbstractEmbedVaadinTomcat {


        private TestableEmbedVaadinServer(EmbedVaadinConfig config) {
            super(config);
        }

        @Override
        protected void configure() {
            initConfiguration();
        }
    }
}
