package pl.hycom.opl.ptp.sog.imports;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

/**
 * Loguje do Alfresco i przechowuje ticket(token) wykorzystaywany do wszystkich kolejnych requestów w celu
 * uwierzytelnienia jako zalogowany. Token dodaje się jako parametr "alf_ticket".
 * http://opl-dev2.hycom.pl/alfresco/service/index/family/Authentication
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class AlfrescoLogin {
    /** URL do zalogowania. */
    private static String LOGIN_URL_TEMPLATE = "http://%s/alfresco/service/api/login?u=%s&pw=%s";
    private static String login = "admin";
    private static String password = "admin";
    /**
     * Token.
     */
    private static String alfTicket;

    /**
     * Logowanie do Alfresco. Zwraca poprawny token jeśli się powiodło.
     * 
     * @param login
     * @param password
     */
    private static String loginAlfresco(String env, String login, String password) {
        String loginUrl = String.format(LOGIN_URL_TEMPLATE, env, login, password);
        try {
            Document doc = Jsoup.connect(loginUrl).parser(Parser.xmlParser()).ignoreContentType(true)
                    .followRedirects(true).get();
            return doc.select("ticket").first().text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTicket(String env) {
        if (alfTicket == null) {
            alfTicket = loginAlfresco(env, login, password);
        }
        return alfTicket;
    }
}
