package br.com.api.meetingroom.handler;

public class ResponseErro {

    private Integer code;
    private String status;
    private String message;

    private ResponseErro(Integer code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseErroBuilder newBuilder() {
        return new ResponseErroBuilder();
    }

    public static final class ResponseErroBuilder {
        private Integer code;
        private String status;
        private String message;

        private ResponseErroBuilder() {
        }

        public ResponseErroBuilder code(Integer code) {
            this.code = code;
            return this;
        }

        public ResponseErroBuilder status(String status) {
            this.status = status;
            return this;
        }

        public ResponseErroBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseErro build() {
            return new ResponseErro(code, status, message);
        }
    }
}
