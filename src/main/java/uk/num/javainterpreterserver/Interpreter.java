package uk.num.javainterpreterserver;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Log4j2
public class Interpreter {

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
                jsonString = interpreter.interpretToJsonString(modl);
                final long end = System.currentTimeMillis();
                log.info(String.format("Took %dms to interpret %s", (end - start), modl));
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
