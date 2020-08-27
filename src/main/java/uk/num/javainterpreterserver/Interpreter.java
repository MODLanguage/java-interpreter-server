package uk.num.javainterpreterserver;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Log4j2
public class Interpreter {

    public static final int MAX_WIDTH = 100;

    public final int MAX_NUM_RECORD_LEN = 25500;

    private final uk.modl.interpreter.Interpreter interpreter = new uk.modl.interpreter.Interpreter();

    @CrossOrigin(origins = "*")
    @GetMapping("/mtoj")
    public String interpretGet(@RequestParam("modl") final String modl) {
        return handler(modl);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/mtoj")
    public String interpretPost(@RequestBody final String modl) {
        return handler(modl);
    }

    private String handler(final String modl) {
        String jsonString = null;
        try {
            if (modl != null) {
                final long start = System.currentTimeMillis();

                final String truncated = StringUtils.truncate(modl, MAX_NUM_RECORD_LEN);

                jsonString = interpreter.interpretToJsonString(truncated);
                final long end = System.currentTimeMillis();
                log.info(String.format("Took %dms to interpret %s", (end - start), StringUtils.truncate(modl, MAX_WIDTH)));
            }

            if (jsonString != null) {
                return jsonString;
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing the supplied MODL string: " + e);
        }

    }

    @GetMapping("/internal/mtoj")
    public String interpretInternalGet(@RequestParam("modl") final String modl) {
        return handler(modl);
    }

    @PostMapping("/internal/mtoj")
    public String interpretInternalPost(@RequestBody final String modl) {
        return handler(modl);
    }

}
