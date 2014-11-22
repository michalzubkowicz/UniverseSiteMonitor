import models.Service;
import org.junit.Test;
import play.Logger;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    /*@Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("Universe Site Monitor");
            }
        });
    }*/
    @Test public void notificationtest() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                Service s = new Service();
                s.setActive(true);
                s.setAddress("http://test.roboczy.com");
                s.setExpectedtext("duddd");
                s.setGuestaccess(true);
                s.setInterval(20);
                s.setLastresponse("OK");
                s.setOk(true);
                s.setName("Testowa usluga");
                s.setNotified(false);
                s.setRetries(0);
                s.save();
                String id = s.getId();
                try {
                    /*Logger.info("Check 1");
                    Check.now();
                    Thread.sleep(10000);
                    Logger.info("Check 2");
                    Check.now();
                    Thread.sleep(10000);
                    Logger.info("Check 3");
                    Check.now();
                    Thread.sleep(10000);*/
                    Service.saveError(id,"error","200");
                    Service.saveError(id,"error","200");
                    Service.saveError(id,"error","200");
                    Service sf= Service.collection.findOneById(id);

                    Logger.debug("RESPONSE: " + sf.getLastresponse());
                    try {
                        Logger.debug("LAST RESPONSE CODE: " + sf.getLastresponsecode());
                    } catch(Exception ignored) {

                    }
                    try {
                        Logger.debug("LAST CHECK: " + sf.getLastcheck().toString());
                    } catch(Exception ignored) {

                    }
                    try {
                        Logger.debug("NOTIFIED: " + sf.getNotified().toString());
                    } catch(Exception ignored) {

                    }
                    try {
                        Logger.debug("RETRIES: " + sf.getRetries().toString());
                    } catch(Exception ignored) {

                    }

                } catch(Exception e) {
                    Logger.error(e.getMessage(),e);
                }
                assertThat(Service.shouldSendNotification(id)).isTrue();



            }
        });
    }

}
