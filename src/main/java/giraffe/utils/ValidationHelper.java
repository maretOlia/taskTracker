package giraffe.utils;

import com.google.common.collect.Sets;
import giraffe.service.activity.InvalidInputException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Set;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
public class ValidationHelper {

    public static void validateInputFields(BindingResult bindingResult) throws InvalidInputException {
        if (bindingResult.hasErrors()) {
            List<ObjectError> errors = bindingResult.getAllErrors();
            Set<String> fields = Sets.newHashSet();
            errors.forEach(error -> {
                if (error instanceof FieldError) fields.add(((FieldError) error).getField());
            });

            throw new InvalidInputException(String.join(", ", fields));
        }
    }

}
