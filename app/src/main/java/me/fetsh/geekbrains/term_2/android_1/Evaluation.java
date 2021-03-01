package me.fetsh.geekbrains.term_2.android_1;

import java.util.List;
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
        String str = Double.toString(getResult());
        if (!str.endsWith(".0")) return str;

        return str.substring(0, str.length() - 2);
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