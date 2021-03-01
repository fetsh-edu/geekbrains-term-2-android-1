package me.fetsh.geekbrains.term_2.android_1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public interface Evaluation {
    NotReady notReady = new NotReady();

    static Evaluation failure(List<String> errors) {
        return new Failure(errors);
    }

    static Evaluation success(Double result) {
        return new Success(result);
    }
}

class NotReady implements Evaluation {

}
class Success implements Evaluation {

    private final Double result;

    public Success(Double result) {
        this.result = result;
    }

    public Double getResult() {
        return result;
    }
    public String getFormattedResult() {
        return Calculator.defaultFormatter.format(getResult());
    }
}
class Failure implements Evaluation {

    private final List<String> errors;

    public Failure(List<String> errors) {
        this.errors = errors;
    }

    public Stream<String> getErrors() {
        return errors.stream();
    }
}