package models;

import net.sf.oval.ConstraintViolation;
import play.libs.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michal Zubkowicz (michal.zubkowicz@gmail.com) on 3/11/14.
 */
public class ValidationException extends Exception {

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    private static List<String> getErrorList(List<ConstraintViolation> errorMessages) {
        List<String> ers = new ArrayList<>();
        for(ConstraintViolation cv : errorMessages) {
            ers.add(cv.getMessage());
        }
        return ers;
    }


    public ValidationException(List<ConstraintViolation> errorMessages) {
        super(Json.toJson(getErrorList(errorMessages)).toString());
    }


}
