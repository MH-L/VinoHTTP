import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Minghao on 7/29/2016.
 */
public class ArticleFilePOSTHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange request) throws IOException {
        String requestMethod = request.getRequestMethod();
        if (!requestMethod.equals("POST")) {
            Utility.displayInvalidRequestMethod(request);
            return;
        }

        // In order to create a new article, first of all the user has to be a registered user.
        List<String> authorization = request.getRequestHeaders().get("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            Utility.displayUnauthorizedAccess(request);
            return;
        }

        String tokenOrSessionID = authorization.get(0);
        String requesterUserName = RedisKeeper.getUserIdentity(tokenOrSessionID);
        if (!DBTransaction.checkIsAdmin(requesterUserName)) {
            Utility.displayUnauthorizedAccess(request);
            return;
        }

        String parametersStr = request.getRequestURI().getQuery();
        Map<String, String> requestMapping = Utility.queryToMap(parametersStr);

        String articleContent = requestMapping.get("content");
        String articleFileName = requestMapping.get("filename");

        if (articleContent == null || articleFileName == null || articleContent.equals("") || articleFileName.equals("")) {
            Utility.displayParamErrorMessage(request, ErrorCodes.ARTICLE_UPLOAD_UNSUCCESSFUL,
                    "The article content and file name must present.");
            return;
        }

        File newFile = new File("/opt/VinoData/Articles/" + articleFileName);
        if (newFile.exists()) {
            Utility.displayParamErrorMessage(request, ErrorCodes.ARTICLE_ALREADY_EXISTS,
                    "The article with the same name already exists!");
            return;
        }
        FileWriter fr = new FileWriter(newFile.getName(), true);
        BufferedWriter bwr = new BufferedWriter(fr);
        bwr.write(articleContent);
        bwr.close();

        String respStr = "{ \"result\": \"Article Upload Finished.\" }";
        request.sendResponseHeaders(302, respStr.getBytes().length);
        OutputStream os = request.getResponseBody();
        os.write(respStr.getBytes());
        os.close();
    }
}
