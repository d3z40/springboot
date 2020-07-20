package com.devdojo.springboot.error;

public class ValidationErrorDetails extends ErrorDetails {
    private String field;
    private String fieldMessage;

    public String getField() {
        return field;
    }

    public String getFieldMessage() {
        return fieldMessage;
    }

    public static final class NotFoundBuilder {
        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developerMessage;
        private String field;
        private String fieldMessage;

        private NotFoundBuilder() {
        }

        public static NotFoundBuilder newBuilder() {
            return new NotFoundBuilder();
        }

        public NotFoundBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotFoundBuilder status(int status) {
            this.status = status;
            return this;
        }

        public NotFoundBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public NotFoundBuilder timestamp(long timetamp) {
            this.timestamp = timetamp;
            return this;
        }

        public NotFoundBuilder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public NotFoundBuilder field(String field) {
            this.field = field;
            return this;
        }

        public NotFoundBuilder fieldMessage(String fieldMessage) {
            this.fieldMessage = fieldMessage;
            return this;
        }

        public ValidationErrorDetails build() {
            ValidationErrorDetails validationErrorDetails = new ValidationErrorDetails();
            validationErrorDetails.setTimestamp(timestamp);
            validationErrorDetails.setDeveloperMessage(developerMessage);
            validationErrorDetails.setDetail(detail);
            validationErrorDetails.setStatus(status);
            validationErrorDetails.setTitle(title);
            validationErrorDetails.field = this.field;
            validationErrorDetails.fieldMessage = this.fieldMessage;

            return validationErrorDetails;
        }
    }
}
