package models;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import java.util.Date;
import java.util.List;

public class Check {

    public static void now() throws Exception{
        final List<Service> services = Service.getActiveServices();

        //TODO: Add threading


        for(final Service service : services) {
            WSRequestHolder holder = WS.url(service.getAddress());
            holder.setTimeout(1000);

            F.Promise<String> jsonPromise = holder.get().map(
                    new F.Function<WSResponse, String>() {
                        public String apply(WSResponse response) {
                            try {
                                Logger.debug("Checking: "+service.getName());
                                boolean expectedFound = true;
                                try {
                                    if (service.getExpectedtext() != null && service.getExpectedtext().length() > 0)
                                        expectedFound = response.getBody().contains(service.getExpectedtext());
                                } catch (NullPointerException e) {
                                    Logger.debug("Error when parsing body: " + e.getMessage(), e);
                                    expectedFound = false;
                                }

                                if (response.getStatus() != 200 || !expectedFound) {
                                    service.setOk(false);
                                    service.setNotified(true);

                                    try {
                                        Email email = new SimpleEmail();
                                        email.setHostName(play.Play.application().configuration().getString("email.host"));
                                        email.setSmtpPort(play.Play.application().configuration().getInt("email.port"));
                                        email.setAuthenticator(new DefaultAuthenticator(play.Play.application().configuration().getString("email.login"), play.Play.application().configuration().getString("email.pass")));
                                        if (play.Play.application().configuration().getInt("email.port") != 25)
                                            email.setSSLOnConnect(true);
                                        email.setFrom(play.Play.application().configuration().getString("email.from"));
                                        email.setSubject("Our website " + service.getName() + " is down!");
                                        email.setMsg("Please visit Universe Site Monitor for more details");
                                        email.addTo(play.Play.application().configuration().getString("email.to"));
                                        email.send();
                                    } catch(Exception me) {
                                        Logger.error("Error when sending notification email "+me.getMessage(),me);
                                    }

                                    Logger.debug("NOTOK: " + response.getStatus() + " " + expectedFound);
                                } else {
                                    service.setOk(true);
                                    service.setNotified(false);
                                    Logger.debug("OK: " + response.getStatus() + " " + expectedFound);
                                }

                                service.setLastcheck(new Date());
                                service.setLastresponse(response.getBody());
                                service.setLastresponsecode(String.valueOf(response.getStatus()));
                                service.save();

                                Response r = new Response();
                                r.setService(service);
                                r.setResponse(response.getBody());
                                r.setResponsecode(String.valueOf(response.getStatus()));
                            } catch(Exception wse) {
                                Logger.error("Error when parsing response from "+service.getName()+": "+wse.getMessage(),wse);
                            }
                            return response.getBody();
                        }
                    }
            );
        }
    }

}
