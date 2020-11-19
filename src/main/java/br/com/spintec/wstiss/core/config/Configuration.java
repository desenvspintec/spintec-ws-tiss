package br.com.spintec.wstiss.core.config;

public class Configuration {
    public static String WS_URL = "";

    public static String getWsUrl(String uri) {
        String wsUrl = WS_URL;
        if (wsUrl.endsWith("/")) {
            wsUrl = wsUrl.substring(0, wsUrl.length() - 1);
        }
        return wsUrl + "/" + uri;
    }
}
