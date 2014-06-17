package models;

import org.mongojack.JacksonDBCollection;
/**
 * Created by Michal Zubkowicz (michal.zubkowicz@gmail.com) on 2/17/14.
 */
public interface AbstractModelInterface {

    static JacksonDBCollection<?, String> collection = null;

    public void save();

    public void remove();

    public void update();

}
