package models;

import play.Logger;

import java.util.Map;

import static play.mvc.Http.Context.Implicit.session;

/**
 * Labels L, used for getting label
 * Created by Michal Zubkowicz (michal.zubkowicz@gmail.com) on 3/11/14.
 */
public enum L {
    INSTANCE;

    private Map<String,Label> labels;
    public static final String default_language="de";

    public String getLabel(String labelname, String defaultvalue, String language) {
        try {
            if(labels==null) {
                this.labels=Label.getAllLabels();
            }

            Label lab = labels.get(labelname);
            if(lab==null) {
                Label l = new Label();
                l.setName(labelname);
                l.setValue(defaultvalue);
                l.save();
                this.labels=Label.getAllLabels();
                return defaultvalue;
            }
            return lab.getTranslatedValue(language);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return defaultvalue;
        }
    }

    public void reloadLabels() {
        this.labels=Label.getAllLabels();
    }

    public static String get(String labelname, String defaultvalue, String language) {
        return L.INSTANCE.getLabel(labelname,defaultvalue,language);
    }

    //same as above just tries guess language
    public static String get(String labelname, String defaultvalue) {
        return L.INSTANCE.getLabel(labelname, defaultvalue, (session().get("language") == null ? default_language : session().get("language"))) ;
    }

    //same as above just tries guess language and default value=labelname
    public static String get(String labelname) {
        return L.INSTANCE.getLabel(labelname, labelname, (session().get("language") == null ? default_language : session().get("language"))) ;
    }
}
