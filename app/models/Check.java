package models;


import play.Logger;
import play.libs.F;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Check {

    public static void now() throws Exception{
        Logger.debug("Running checks: "+new Date());
        final List<Service> services = Service.getActiveServices();

        //TODO: Add threading
        final Map<String,Long> times=new HashMap<>();

        for(final Service service : services) {
            WSRequestHolder holder = WS.url(service.getAddress());
            times.put(service.getId(), new Date().getTime());
            holder.setTimeout(5000);

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
                                r.setResponsetime(new Date().getTime()-times.get(service.getId()));
                                r.setService(service);
                                r.setResponse(response.getBody());
                                r.setResponsecode(String.valueOf(response.getStatus()));

                                r.save();
                            } catch(Exception wse) {
                                Logger.error("Error when parsing response from "+service.getName()+": "+wse.getMessage(),wse);
                            }
                            return response.getBody();
                        }
                    }
            );
            jsonPromise.recover(new F.Function<Throwable, String>() {
                @Override
                public String apply(Throwable throwable) throws Throwable {
                   service.setOk(false);
                   service.setLastresponsecode("0");
                   service.setLastresponse("");
                   service.save();
                   Logger.debug("Error:"+throwable.getCause().toString());
                   return "error";
                }
            });

        }
    }

}
