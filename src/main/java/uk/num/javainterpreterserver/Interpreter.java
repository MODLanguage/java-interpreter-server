package uk.num.javainterpreterserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.modl.parser.errors.InterpreterError;

@RestController
@Log4j2
public class Interpreter {

    public static final int MAX_WIDTH = 100;

    public static final int MAX_NUM_RECORD_LEN = 25500;

    private final uk.modl.interpreter.Interpreter modlInterpreter = new uk.modl.interpreter.Interpreter();

    @Setter
    @Value("${interpreter.timeoutMilliseconds}")
    private int timeoutMilliseconds = 5000;

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

                jsonString = modlInterpreter.interpretToJsonString(truncated, null, timeoutMilliseconds);
                final long end = System.currentTimeMillis();
                log.info(String.format("Took %dms to interpret %s", (end - start), StringUtils.truncate(modl, MAX_WIDTH) + "..."));
            }

            if (jsonString != null) {
                return jsonString;
            } else {
                return "";
            }
        } catch (final InterpreterError | Error | JsonProcessingException e) {
            if (e.getMessage() != null && e.getMessage()
                    .contains("java.util.concurrent.TimeoutException")) {
                log.info("Aborting the request because it was taking too long to process: '{}'", StringUtils.truncate(modl, MAX_WIDTH) + "...");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Internal Error processing the supplied MODL string. Try simplifying your MODL.");
            } else if (e.getMessage() != null && e.getMessage()
                    .contains("java.lang.StackOverflowError")) {
                log.info("Aborting the request because a serious error has occurred interpreting this: '{}'", StringUtils.truncate(modl, MAX_WIDTH) + "... " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Internal Error processing the supplied MODL string. Try simplifying your MODL.");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing the supplied MODL string: " + e);
            }
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
