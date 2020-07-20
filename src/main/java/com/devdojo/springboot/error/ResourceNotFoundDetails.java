package com.devdojo.springboot.error;

public class ResourceNotFoundDetails extends ErrorDetails {

    public static final class NotFoundBuilder {
        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developerMessage;

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

        public ResourceNotFoundDetails build() {
            ResourceNotFoundDetails resourceNotFoundDetails = new ResourceNotFoundDetails();
            resourceNotFoundDetails.setTimestamp(timestamp);
            resourceNotFoundDetails.setDeveloperMessage(developerMessage);
            resourceNotFoundDetails.setDetail(detail);
            resourceNotFoundDetails.setStatus(status);
            resourceNotFoundDetails.setTitle(title);

            return resourceNotFoundDetails;
        }
    }
}
