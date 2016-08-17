import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Minghao on 3/11/2016.
 */
public class Utility {
    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

    public static void displayParamErrorGeneral(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"400\", \"reason\": \"Bad Request -- Invalid Parameter\" }";
        request.sendResponseHeaders(404, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayParamErrorMessage(HttpExchange request, int errorCode, String message) throws IOException {
        String response = String.format("{ \"error\": \"%s\", \"reason\": \"%s\" }", errorCode, message);
        request.sendResponseHeaders(404, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayInternalServerError(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"500\", \"reason\": \"Internal Server Error\" }";
        request.sendResponseHeaders(502, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayAccountExistsError(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"406\", \"reason\": \"Account Exists\" }";
        request.sendResponseHeaders(406, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayInvalidRequestMethod(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"405\", \"reason\": \"Invalid Request Method\" }";
        request.sendResponseHeaders(405, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayUnauthorizedAccess(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"401\", \"reason\": \"Unauthorized\" }";
        request.sendResponseHeaders(401, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    public static void displayAccessForbidden(HttpExchange request) throws IOException {
        String response = "{ \"error\": \"403\", \"reason\": \"Forbidden\" }";
        request.sendResponseHeaders(403, response.getBytes().length);
        OutputStream outStr = request.getResponseBody();
        outStr.write(response.getBytes());
        outStr.close();
    }

    /**
     * Replaces characters that may be confused by an SQL
     * parser with their equivalent escape characters.
     * <p>
     * Any data that will be put in an SQL query should
     * be be escaped.  This is especially important for data
     * that comes from untrusted sources such as Internet users.
     * <p>
     * For example if you had the following SQL query:<br>
     * <code>"SELECT * FROM addresses WHERE name='" + name + "' AND private='N'"</code><br>
     * Without this function a user could give <code>" OR 1=1 OR ''='"</code>
     * as their name causing the query to be:<br>
     * <code>"SELECT * FROM addresses WHERE name='' OR 1=1 OR ''='' AND private='N'"</code><br>
     * which will give all addresses, including private ones.<br>
     * Correct usage would be:<br>
     * <code>"SELECT * FROM addresses WHERE name='" + StringHelper.escapeSQL(name) + "' AND private='N'"</code><br>
     * <p>
     * Another way to avoid this problem is to use a PreparedStatement
     * with appropriate placeholders.
     *
     * @param s String to be escaped
     * @return escaped String
     * @throws NullPointerException if s is null.
     *
     * @since ostermillerutils 1.00.00
     */
    public static String escapeSQL(String s){
        int length = s.length();
        int newLength = length;
        // first check for characters that might
        // be dangerous and calculate a length
        // of the string that has escapes.
        for (int i=0; i<length; i++){
            char c = s.charAt(i);
            switch(c){
                case '\\':
                case '\"':
                case '\'':
                case '\0':{
                    newLength += 1;
                } break;
            }
        }
        if (length == newLength){
            // nothing to escape in the string
            return s;
        }
        StringBuffer sb = new StringBuffer(newLength);
        for (int i=0; i<length; i++){
            char c = s.charAt(i);
            switch(c){
                case '\\':{
                    sb.append("\\\\");
                } break;
                case '\"':{
                    sb.append("\\\"");
                } break;
                case '\'':{
                    sb.append("\\\'");
                } break;
                case '\0':{
                    sb.append("\\0");
                } break;
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String getFirstSubRoute(HttpExchange request) {
        String reqQueryList = request.getRequestURI().getPath().
                substring(request.getHttpContext().getPath().length());
        String[] parts = reqQueryList.split("/");
        if (parts.length < 2) {
            return null;
        }
        return parts[1];
    }
}

