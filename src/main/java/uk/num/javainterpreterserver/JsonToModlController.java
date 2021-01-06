package uk.num.javainterpreterserver;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.num.json_modl.converter.JsonToModl;

@RestController
@Log4j2
public class JsonToModlController {

    private final JsonToModl converter = new JsonToModl();

    @CrossOrigin(origins = "*")
    @GetMapping("/jtom")
    public ResponseEntity<String> jsonToModlGet(@RequestParam("json") final String json) {
        return handler(json);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/jtom")
    public ResponseEntity<String> jsonToModlPost(@RequestBody final String json) {
        return handler(json);
    }

    @GetMapping("/internal/jtom")
    public ResponseEntity<String> jsonToModlInternalGet(@RequestParam("json") final String json) {
        return handler(json);
    }

    @PostMapping("/internal/jtom")
    public ResponseEntity<String> jsonToModlInternalPost(@RequestBody final String json) {
        return handler(json);
    }

    private ResponseEntity<String> handler(final String json) {
        try {
            final long start = System.currentTimeMillis();
            final String modl = converter.pairToModl(json)
                    .replace("\\", "\\\\");
            final long end = System.currentTimeMillis();
            log.info(String.format("Took %dms to convert to %s", (end - start), modl));

            return ResponseEntity.ok()
                    .body("{\"modl\":\"" + StringEscapeUtils.escapeJava(modl) + "\"}");
        } catch (final Exception e) {
            log.error("Error converting JSON to MODL.", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

}
