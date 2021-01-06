package uk.num.javainterpreterserver;

import org.junit.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.*;

public class InterpreterTest {

  public static final String GOOD_MODL = "a=b";

  public static final String BAD_MODL = "a=b]";

  public static final String TIMEOUT_MODL = "a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a=a";

  private final Interpreter interpreter = new Interpreter();

  @Test
  public void testSuccess() {
    interpreter.setTimeoutMilliseconds(5000);
    final String result = interpreter.interpretGet(GOOD_MODL);
    assertEquals("{\"a\":\"b\"}", result);
  }

  @Test
  public void testBadModl() {
    try {
      interpreter.setTimeoutMilliseconds(5000);
      interpreter.interpretGet(BAD_MODL);
    } catch (final ResponseStatusException e) {
      assertEquals(400, e.getStatus().value());
      assertEquals(
          "400 BAD_REQUEST \"Error processing the supplied MODL string: uk.modl.parser.errors.InterpreterError: Parser Error: line 1:3 no viable alternative at input 'a=b]'\"",
          e.getMessage());
    }
  }

  @Test
  public void testTimeout() {
    try {
      interpreter.setTimeoutMilliseconds(100);
      interpreter.interpretGet(TIMEOUT_MODL);
    } catch (final ResponseStatusException e) {
      assertEquals(400, e.getStatus().value());
      assertEquals("400 BAD_REQUEST \"Internal Error processing the supplied MODL string. Try simplifying your MODL.\"",
          e.getMessage());
    }
  }

}