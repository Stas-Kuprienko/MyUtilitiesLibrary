package stas.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class HttpWebException extends Exception {

    private static final String messageAttrib = "message";
    private static final String headerAttrib = "header";
    private static final String codeAttrib = "code";

    public final int CODE;

    public final ERROR error;


    public HttpWebException(int CODE) {
        this.CODE = CODE;
        this.error = choose(CODE);
    }

    public HttpWebException(ERROR error) {
        this.CODE = error.code;
        this.error = error;
    }


    public void errorRedirect(String errorPageURL, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(codeAttrib, error.code);
        request.setAttribute(headerAttrib, error.header);
        request.setAttribute(messageAttrib, error.message);
        request.getRequestDispatcher(errorPageURL).forward(request, response);
    }

    private static ERROR choose(int code) {
        for (ERROR e : ERROR.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return ERROR.SERVER_ERROR;
    }

    public enum ERROR {

        SERVER_ERROR(500, "Internal Server Error", "Sorry, an error occurred on the server"),
        BAD_REQUEST(400, "Bad Request", "Sorry, you have made an incorrect request"),
        UNAUTHORIZED(401, "Unauthorized", "Sorry, the request is made by an unauthorized user"),
        FORBIDDEN(403, "Forbidden", "Sorry, you cannot make this request"),
        NOT_FOUND(404, "Not Found", "Sorry, that you requested is not found"),
        NOT_ALLOWED(405, "Not Allowed", "Sorry, the method cannot be applied to the current resource");


        public final int code;
        public final String header;
        public final String message;

        ERROR(int code, String header, String message) {
            this.code = code;
            this.header = header;
            this.message = message;
        }
    }
}