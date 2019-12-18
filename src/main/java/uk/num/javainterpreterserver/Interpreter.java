package uk.num.javainterpreterserver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

@RestController
public class Interpreter {

    @CrossOrigin(origins = "*")
    @GetMapping("/")
    public String interpretGet(@RequestParam("modl") final String modl) {
        return handler(modl);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/")
    public String interpretPost(@RequestBody final String modl) {
        return handler(modl);
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/ready")
    public ResponseEntity<String> ready() {
        final String json = handler("a=b");
        if (StringUtils.isNotEmpty(json)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
    }


    private String handler(final String modl) {
        ModlObject modlObject = null;
        try {
            if (modl != null) {
                modlObject = uk.modl.interpreter.Interpreter.interpret(modl);
            }

            if (modlObject != null) {
                return JsonPrinter.printModl(modlObject);
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing the supplied MODL string: " + e);
        }

    }
}
