package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mongojack.JacksonDBCollection;
import play.Logger;
import play.modules.mongojack.MongoDB;

import java.util.HashMap;
import java.util.Map;


public class Label extends AbstractModel implements AbstractModelInterface {

    public static JacksonDBCollection<Label, String> collection = MongoDB.getCollection("labels", Label.class, String.class);

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String pl;

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    private String de;

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    private String en;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getTranslatedValue(String lang) {
        switch (lang) {
            case "en" : return this.en;
            case "de" : return this.de;
            case "pl" : return this.pl;
        }
        return "There is no translation in language "+lang;
    }

    public void setValue(String s) {
        this.de=s;
        this.en=s;
        this.pl=s;
    }

    public static Map<String,String> getLabels() {
        return Label.getLabels("en");
    }


    public static Map<String,String> getLabels(String lang) {
        Map<String,String> labels=new HashMap<>();
        try {
            for (Label i : collection.find()) {
                labels.put(i.getName(), i.getTranslatedValue(lang));
            }
        } catch(Exception e) {
            Logger.error(e.getMessage(), e);
        }

        return labels;
    }

    public static Map<String,Label> getAllLabels() {
        Map<String,Label> labels=new HashMap<>();
        try {
            for (Label i : collection.find()) {
                labels.put(i.getName(), i);
            }
        } catch(Exception e) {
            Logger.error(e.getMessage(), e);
        }

        return labels;
    }

    public void save() {
        if(this.id==null) {
            this.id = collection.insert(this).getSavedId();
        } else {
            this.update();
        }
    }

    public void update() {
        collection.updateById(this.id,this);
        try {
            L.INSTANCE.reloadLabels();
        } catch(Exception e) {
            Logger.error(e.getMessage());
        }
    }


    public void remove() {
        collection.removeById(this.id);
    }


    public static void removeById(String id) {
        collection.removeById(id);
    }

    public static Label createFromJson(JsonNode j) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if(j.hasNonNull("_id")) {
            Label g = collection.findOneById(j.get("_id").asText());
            return mapper.readerForUpdating(g).readValue(j.toString());
        } else {
            return mapper.readValue(j.toString(), Label.class);
        }
    }

}