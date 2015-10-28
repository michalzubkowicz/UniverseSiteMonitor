package models;


import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import play.Logger;
import play.Play;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import java.util.*;

public class Check {

    public static void now() throws Exception{
        Logger.debug("Running checks: "+new Date());
        final List<Service> services = Service.getActiveServices();

        //TODO: Add threading & rewrite
        final Map<String,Long> times=new HashMap<>();
        final List<String> doneservices=new ArrayList<>();
        final List<String> notifyservices = new ArrayList<>();


        for(final Service service : services) {
            Logger.debug("CHECKING URL: " + service.getAddress());
            try {
                WSRequestHolder holder = WS.url(service.getAddress());
                times.put(service.getId(), new Date().getTime());
                try {
                    holder.setTimeout(Play.application().configuration().getInt("check.timeout") * 1000);
                } catch (Exception e) {
                    Logger.error("Wrong timeout value, using default: 1s");
                    holder.setTimeout(1000);
                }

                F.Promise<String> jsonPromise = holder.get().map(
                        new F.Function<WSResponse, String>() {
                            public String apply(WSResponse response) {
                                try {
                                    Logger.debug("Checking: " + service.getName());
                                    boolean expectedFound = true;
                                    try {
                                        if (service.getExpectedtext() != null && service.getExpectedtext().length() > 0)
                                            expectedFound = response.getBody().contains(service.getExpectedtext());
                                    } catch (NullPointerException e) {
                                        Logger.debug("Error when parsing body: " + e.getMessage(), e);
                                        expectedFound = false;
                                    }


                                    if (response.getStatus() != 200 || !expectedFound) {
                                        Service.saveError(service.getId(), response.getBody(), String.valueOf(response.getStatus()));
                                        Service.shouldSendNotification(service.getId());
                                        Logger.debug("NOTOK: " + response.getStatus() + " " + expectedFound);
                                    } else {
                                        Service.saveOK(service.getId());
                                        Logger.debug("OK: " + response.getStatus() + " " + expectedFound);
                                    }


                                    Response r = new Response();
                                    r.setResponsetime(new Date().getTime() - times.get(service.getId()));
                                    r.setService(service);
                                    r.setResponse(response.getBody());
                                    r.setResponsecode(String.valueOf(response.getStatus()));
                                    r.save();

                                } catch (NullPointerException wsne) {
                                    Service.saveError(service.getId(), "", "0");
                                    Service.shouldSendNotification(service.getId());
                                }
                                catch (Exception wse) {
                                    Service.saveError(service.getId(), response.getBody(), String.valueOf(response.getStatus()));
                                    Service.shouldSendNotification(service.getId());
                                    Logger.error("Error when parsing response from " + service.getName() + ": " + wse.getMessage(), wse);
                                }
                                doneservices.add(service.getName());
                                if (doneservices.size() == services.size()-1 && notifyservices.size() > 0)
                                    //if(notifyservices.size()>0)
                                    Check.sendNotification(notifyservices);
                                return response.getBody();
                            }
                        }
                );
                jsonPromise.recover(new F.Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Throwable {
                        Service.saveError(service.getId(), "", "0");
                        doneservices.add(service.getName());

                        if (Service.shouldSendNotification(service.getId())) notifyservices.add(service.getName());
                        if (doneservices.size() == services.size() && notifyservices.size() > 0)
                            Check.sendNotification(notifyservices);

                        Response r = new Response();
                        r.setResponsetime((long) 0);
                        r.setService(service);
                        r.setResponse("");
                        r.setResponsecode("0");
                        r.save();
                        Logger.debug("Error:" + throwable.getCause().toString());
                        return "error";
                    }
                });
            } catch(Exception fe) {
                Logger.error(fe.getMessage(),fe);
            }

        }
    }

    public static void sendNotification(List<String> body) {

        StringBuilder sb = new StringBuilder("Sites:\n");
        for(String s : body) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append("are unavailable");
        sendNotification(sb.toString());
    }


    public static void sendNotification(String body) {
        try {

            Email email = new SimpleEmail();
            email.setHostName(play.Play.application().configuration().getString("email.host"));
            email.setSmtpPort(play.Play.application().configuration().getInt("email.port"));
            email.setAuthenticator(new DefaultAuthenticator(play.Play.application().configuration().getString("email.login"), play.Play.application().configuration().getString("email.pass")));
            if (play.Play.application().configuration().getInt("email.port") != 25)
                email.setSSLOnConnect(true);
            email.setFrom(play.Play.application().configuration().getString("email.from"));
            email.setSubject("Sites unavailable!");
            email.setMsg(body+"\nPlease visit Universe Site Monitor for more details");
            email.addTo(play.Play.application().configuration().getString("email.to"));
            email.send();
        } catch (Exception me) {
            Logger.error("Error when sending notification email " + me.getMessage(), me);
        }
    }

}
