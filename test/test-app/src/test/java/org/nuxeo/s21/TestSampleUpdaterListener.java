package org.nuxeo.s21;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.event.EventService;
import org.nuxeo.ecm.core.event.impl.EventListenerDescriptor;
import org.nuxeo.ecm.platform.test.PlatformFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;

@RunWith(FeaturesRunner.class)
@Features({ PlatformFeature.class })
@Deploy({"org.nuxeo.s21.s2i-test-app"})
public class TestSampleUpdaterListener {

    protected final List<String> events = Arrays.asList("documentCreated");

    @Inject
    protected EventService s;

    @Inject
    CoreSession session;

    @Test
    public void listenerRegistration() {
        EventListenerDescriptor listener = s.getEventListener("sampleupdaterlistener");
        assertNotNull(listener);
        assertTrue(events.stream().allMatch(listener::acceptEvent));
    }

    @Test
    public void noteDescirptionShouldBeUpdated() throws Exception {
    	DocumentModel doc = session.createDocumentModel("/","anote","Note");
    	doc = session.createDocument(doc);
    	doc = session.getDocument(new PathRef("/anote"));
    	assertEquals("Updated Value", doc.getProperty("dc:description").getValue(String.class));
    }


}
