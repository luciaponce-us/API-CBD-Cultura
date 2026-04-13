package com.tfg.cultura.api.users.validation.validators;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.tfg.cultura.api.users.validation.annotations.ValidDni;

public class ValidDniValidator implements ConstraintValidator<ValidDni, String> {

    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }

        String regex = "^\\d{8}[A-Za-z]$";
        if (!dni.matches(regex)) {
            return false;
        }

        String numberPart = dni.substring(0, 8);
        char letterPart = Character.toUpperCase(dni.charAt(8));

        String letters = "TRWAGMYFPDXBNJZSQVHLCKE";
        int index = Integer.parseInt(numberPart) % 23;

        return letterPart == letters.charAt(index);
    }
    
}
