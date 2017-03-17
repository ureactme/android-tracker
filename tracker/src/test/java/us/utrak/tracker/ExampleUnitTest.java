package us.utrak.tracker;

import java.net.InetAddress;
import java.net.URL;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() throws Exception {
    URL url = new URL("https://app.ureact.me");
    InetAddress ip = InetAddress.getByName(url.getHost());
    System.out.println(ip);
    assertEquals(true, !ip.equals(""));
  }
}