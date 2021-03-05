package me.fetsh.geekbrains.term_2.android_1;

public class OperatorFormatException extends IllegalArgumentException {

    /**
     * Constructs a <code>OperatorFormatException</code> with no detail message.
     */
    public OperatorFormatException () {
        super();
    }

    /**
     * Constructs a <code>OperatorFormatException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public OperatorFormatException (String s) {
        super (s);
    }

    /**
     * Factory method for making a <code>OperatorFormatException</code>
     * given the specified input which caused the error.
     *
     * @param   s   the input causing the error
     */
    static OperatorFormatException forInputString(String s) {
        return new OperatorFormatException("Illegal operator: \"" + s + "\"");
    }
}
