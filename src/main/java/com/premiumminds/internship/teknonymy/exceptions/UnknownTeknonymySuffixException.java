package com.premiumminds.internship.teknonymy.exceptions;

/**
 * Exception: could not define teknonymy suffix "mother" or "father" due to
 * unknown character.
 */
public class UnknownTeknonymySuffixException extends Exception {
    private Character _sex;
    private String _person;

    /**
     * Default constructor.
     * @param sex
     * @param person
     */
    public UnknownTeknonymySuffixException(Character sex, String person) {
        _sex = sex;
        _person = person;
    }

    /**
     * @return error message clarifying the exception.
     */
    public String errorMessage() {
        if (_sex == null)
            return "Error: could not obtain teknonymy suffix for " + _person + " from null";
        return "Error: could not obtain teknonymy suffix for " + _person + " from '"  + _sex + "' Character";
    }
}