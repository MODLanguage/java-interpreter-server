package uk.num.javainterpreterserver;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class JsonToModlControllerTest {

    private final JsonToModlController controller = new JsonToModlController();

    @Test
    public void testSuccess() {
        final ResponseEntity<String> result = controller.jsonToModlGet("{\"a\":\"b\"}");
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("{\"modl\":\"a=b\"}", result.getBody());
    }

    @Test
    public void testWithEmbeddedNewline() {
        final ResponseEntity<String> result = controller.jsonToModlGet("{\"a\":\"Hello\\nWorld\"}");
        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("{\"modl\":\"a=Hello\\\\nWorld\"}", result.getBody());
    }

    @Test
    public void testWithBadJson() {
        final ResponseEntity<String> result = controller.jsonToModlGet("{\"a\":Hello World\"}");
        assertNotNull(result);
        assertEquals(400, result.getStatusCodeValue());
        assertNull(result.getBody());
    }

}