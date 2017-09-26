package pl.hycom.opl.ptp.sog.imports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import pl.hycom.opl.ptp.sog.model.SimpleOffer;

/**
 * Służy do sprawdzania poprawności importu. Sprawdzenie polega na zalogowaniu do alfresco, pobraniu wszystkich ofert z
 * danego katalogu (parsowanie XML) i porównaniu ich nazw do nazw ofert przeznaczonych do zaimportowania.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class ImportValidator {

    /**
     * URL do wyświetlenia dzieic folderu "%s". Zwraca XML z pełniymi informacjami o folderze wraz z
     * dokumentami-dziećmi.
     */
    private static String CHILDREN_URL_TEMPLATE = "http://%s/alfresco/service/cmis/i/%s/children?alf_ticket=%s";

    /**
     * Sprawdza poprawność wykonanego importu ofert. Porownuje czy oferty przeparsowane z pliku widnieją jako dzieci w
     * odpowiednim folderze Alfresco.
     * 
     * @param destination
     *            identyfikator folderu Alfresco w którym szukamy ofert
     *            (workspace://SpacesStore/xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)
     * @param offers
     *            lista ofert wczytanych z pliku, których obecności spodziewamy się w Alfresco.
     */
    public boolean isImportCorrect(String env, List<SimpleOffer> offers, String destination) {
        try {
            // Zamieniam "workspace://SpacesStore/xxx" na "xxx"
            String shortDestinationId = destination.replace("workspace://SpacesStore/", "");
            // Pobieram dokument XML i parsuję go używając Jsoup do jego obiektowej reprezentacji
            String url = String.format(CHILDREN_URL_TEMPLATE, env, shortDestinationId, AlfrescoLogin.getTicket(env));
            Document doc = Jsoup.connect(url).parser(Parser.xmlParser()).ignoreContentType(true).followRedirects(true)
                    .get();
            System.out.println("sprawdzam, czy jest " + offers.size() + " ofert w folderze " + shortDestinationId
                    + " // " + url);
            // z DOM wyciągam wszystkie oferty widniejące jako <entry></entry>
            Elements xmlElements = doc.select("entry");
            if (xmlElements.size() != offers.size()) {
                System.out.println(" offers.size() " + offers.size() + "\t\telements.size() " + xmlElements.size());
            }

            List<String> importedOffersTitles = new ArrayList<String>();
            // tworzę listę nazw wszystkich ofert z Alfresco
            for (Element element : xmlElements) {
                // wyciągam nazwę oferty z XML
                Element title = element.select("[propertyDefinitionId=\"cmis:name\"]").first();
                importedOffersTitles.add(title.text());
            }
            // sprawdzam czy każda z ofert przeznaczonych do importu ma swój odpowienik w Alfresco
            for (SimpleOffer offer : offers) {
                if (!importedOffersTitles.contains(offer.getName())) {
                    System.out.println("  w Alfresco nie ma oferty o nazwie: " + offer.getName());
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}
