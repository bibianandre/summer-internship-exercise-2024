package com.premiumminds.internship.teknonymy.exceptions;

/**
 * Exception: child is older than parent.
 */
public class ParentChildAgeException extends Exception {
    private String _parent;
    private String _child;

    /**
     * Default constructor.
     * @param child 
     * @param parent 
     */
    public ParentChildAgeException(String child, String parent) {
        _child = child;
        _parent = parent;
    }

    /**
     * @return error message clarifying the exception.
     */
    public String errorMessage() {
        return "Error: descendant " + _child + " is older than parent " + _parent;
    }
}