package stas.http_tools;

import stas.exceptions.HttpWebException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static stas.http_tools.HttpRequester.Property.*;

public class HttpRequester {

    private final String appForm;
    private final String url;

    private String authorization = "Bearer ";
    private String charset = "UTF-8";

    private HttpRequester(APP_FORM appForm, String url) {
        this.appForm = appForm.value;
        this.url = url;
    }

    public static HttpRequester getJSONRequester(String url) {
        return new HttpRequester(APP_FORM.JSON, url);
    }

    public static HttpRequester getXWWWFormRequester(String url) {
        return new HttpRequester(APP_FORM.X_WWW_FORM, url);
    }


    public String sendHttpGetRequest(String jwt, String resource, String query) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        query = query == null || query.isEmpty() ? "" : "/?" + query;
        try {
            URL url = new URL(this.url + resource + query);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty(AUTHORIZATION.value, authorization + jwt);
            connection.setRequestProperty(CONTENT_TYPE.value, appForm);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String sendHttpPostRequest(String resource, String requestBody) throws IOException, HttpWebException {
        String method = "POST";
        return sendHttpRequest(method, resource, requestBody);
    }

    public String sendHttpPostRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "POST";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public String sendHttpPutRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "PUT";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public String sendHttpDeleteRequest(String jwt, String resource, String requestBody) throws IOException, HttpWebException {
        String method = "DELETE";
        return sendHttpRequest(jwt, method, resource, requestBody);
    }

    public void download(String jwt, String resource, OutputStream output) throws IOException, HttpWebException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty(AUTHORIZATION.value, authorization + jwt);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(connection.getInputStream());

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } else {
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String sendHttpRequest(String method, String resource, String requestBody) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty(CONTENT_TYPE.value, appForm);
            connection.setRequestProperty(CHARSET.value, charset);
            connection.setRequestProperty(CONTENT_LENGTH.value, String.valueOf(requestBodyBytes.length));
            OutputStream output = connection.getOutputStream();
            output.write(requestBodyBytes);
            output.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    private String sendHttpRequest(String jwt, String method, String resource, String requestBody) throws IOException, HttpWebException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(this.url + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty(AUTHORIZATION.value, authorization + jwt);
            connection.setRequestProperty(CONTENT_TYPE.value, appForm);
            connection.setRequestProperty(CHARSET.value, charset);
            OutputStream output;
            if (requestBody != null && !requestBody.isEmpty()) {
                byte[] requestBodyBytes = requestBody.getBytes(StandardCharsets.UTF_8);
                connection.setRequestProperty(CONTENT_LENGTH.value, String.valueOf(requestBodyBytes.length));
                output = connection.getOutputStream();
                output.write(requestBodyBytes);
            } else {
                output = connection.getOutputStream();
            }
            output.flush();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            } else {
                throw new HttpWebException(responseCode);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    enum Property {

        AUTHORIZATION("Authorization"),
        CONTENT_TYPE("Content-Type"),
        CHARSET("charset"),
        CONTENT_LENGTH("Content-Length");

        final String value;

        Property(String value) {
            this.value = value;
        }
    }

    enum APP_FORM {

        X_WWW_FORM("application/x-www-form-urlencoded"),
        JSON("application/json");

        final String value;

        APP_FORM(String value) {
            this.value = value;
        }
    }
}
