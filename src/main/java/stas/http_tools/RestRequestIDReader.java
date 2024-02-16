package stas.http_tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RestRequestIDReader {

    private final Pattern regExIdPattern;

    public RestRequestIDReader(String resource) {
        this.regExIdPattern = Pattern.compile(resource + "/(\\d*)");
    }

    public Integer getId(String pathInfo) {
        Matcher matcher;

        matcher = regExIdPattern.matcher(pathInfo);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return -1;
        }
    }
}