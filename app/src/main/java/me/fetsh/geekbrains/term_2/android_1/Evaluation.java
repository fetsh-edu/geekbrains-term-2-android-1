package me.fetsh.geekbrains.term_2.android_1;

import androidx.annotation.NonNull;

public interface Evaluation {

    NotReady notReady = new NotReady();

    static Evaluation failure(@NonNull String error) {
        return new Failure(error);
    }

    static Evaluation success(@NonNull Double result) {
        return new Success(result);
    }

    class NotReady implements Evaluation {
        private NotReady(){}
    }

    class Success implements Evaluation {

        private final Double result;

        private Success(@NonNull Double result) {
            this.result = result;
        }

        public Double getResult() {
            return result;
        }

        public String getFormattedResult() {
            return Formatter.format(getResult());
        }
    }

    class Failure implements Evaluation {

        private final String error;

        private Failure(@NonNull String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}