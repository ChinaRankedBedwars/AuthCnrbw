package iafenvoy.authcnrbw;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalServer {
    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/get", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {

            try {
                String queryString = exchange.getRequestURI().getQuery();
                Map<String, String> queryStringInfo = formData2Dic(queryString);

                String ign = queryStringInfo.get("ign");
                boolean success = CodeEventHandler.codes.containsKey(ign) && new Date().getTime() - CodeEventHandler.codes.get(ign).time.getTime() <= 15 * 60 * 1000;

                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                if (success)
                    os.write(("{\"success\":true,\"data\":\"" + CodeEventHandler.codes.get(ign).code + "\"}").getBytes());
                else if (ign.equalsIgnoreCase("ign"))
                    os.write(("{\"success\":false,\"data\":\"\\u4f60\\u90fd\\u8f93\\u5165\\u4e86\\u4e9b\\u5565\\uff1f\\uff1f\\uff1f\"}").getBytes());
                else
                    os.write(("{\"success\":false,\"data\":\"\\u9a8c\\u8bc1\\u7801\\u4e0d\\u5b58\\u5728\\u6216\\u5df2\\u8fc7\\u671f\\uff0c\\u8bf7\\u91cd\\u65b0\\u83b7\\u53d6\"}").getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> formData2Dic(String formData) {
        Map<String, String> result = new HashMap<>();
        if (formData == null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        Arrays.stream(items).forEach(item -> {
            final String[] keyAndVal = item.split("=");
            if (keyAndVal.length == 2) {
                final String key = URLDecoder.decode(keyAndVal[0], StandardCharsets.UTF_8);
                final String val = URLDecoder.decode(keyAndVal[1], StandardCharsets.UTF_8);
                result.put(key, val);
            }
        });
        return result;
    }
}