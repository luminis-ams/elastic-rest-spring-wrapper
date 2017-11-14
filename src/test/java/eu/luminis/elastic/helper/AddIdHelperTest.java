package eu.luminis.elastic.helper;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddIdHelperTest {

    @Test
    public void testAddIdToEntityWithMethodPresent() throws Exception {
        MessageWithoutId messageWithoutId = new MessageWithoutId();
        AddIdHelper.addIdToEntity("test_id",messageWithoutId, "setTitle");

        assertEquals("test_id", messageWithoutId.getTitle());
    }

    @Test
    public void testAddIdToEntityNoMethodPresent() throws Exception {
        MessageWithoutId messageWithoutId = new MessageWithoutId();
        try {
            AddIdHelper.addIdToEntity("test_id", messageWithoutId);
            fail("We expected a HelperException");
        } catch (HelperException e) {
            assertEquals("The provided method 'setId' is not available.", e.getMessage());
        }
    }


    public class MessageWithoutId {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}