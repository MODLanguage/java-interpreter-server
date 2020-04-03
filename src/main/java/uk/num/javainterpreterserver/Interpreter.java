package uk.num.javainterpreterserver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.modl.modlObject.ModlObject;
import uk.modl.parser.printers.JsonPrinter;

@RestController
public class Interpreter {

    @GetMapping("/")
    public String interpretGet(@RequestParam("modl") final String modl) {
        return handler(modl);
    }

    @PostMapping("/")
    public String interpretPost(@RequestBody final String modl) {
        return handler(modl);
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
