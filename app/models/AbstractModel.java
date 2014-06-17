package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.mongojack.ObjectId;

import java.util.List;

/**
 * Created by Michal Zubkowicz (michal.zubkowicz@gmail.com) on 3/11/14.
 */
public class AbstractModel  {
    protected String id;

    @ObjectId
    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @ObjectId
    @JsonProperty("_id")
    public void setId(String id) {
        this.id=id;
    }

    public void validate() throws ValidationException {
        Validator validator = new Validator();
        List<ConstraintViolation> l = validator.validate(this);
        if(l.size()>0) throw new ValidationException(l);
    }

}
