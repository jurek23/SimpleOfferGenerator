package pl.hycom.opl.ptp.sog.imports;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import pl.hycom.opl.ptp.sog.model.SimpleOffer;
import pl.hycom.opl.ptp.sog.util.HttpFileUploader;

/**
 * Właściwa logika importu Alfresco do konkretnego folderu (np. neo-ins lob 3p-mod)
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class ImportRunnable implements Runnable {
    /** Komponent zbierający statusy importu dla każdego z formularzy */
    private ImportStatus status;
    /** Numer wątku, żeby wiedzieć którego wątku dotyczy zwracany status. */
    private int i;
    /** Komponent w GUI, który prezentować będzie na bieżąco status importu. */
    private JTextComponent log;
    /** Środowisko */
    private String env;
    /** Szablon URLa do pobrania dzieci ofpowiedniego folderu. */
    private String urlTemplate;
    /** Plik z ofertami, który zostanie przesłany i zapiportowany do alfresco. */
    private File fileToImport;
    /**
     * Te oferty NIE są tu przekazywane w celu importu a jedynie do walidacji poprawności importu. Oferty które zostaną
     * uploadowane są już zapisane jako pliki w %TEMP%
     */
    private List<SimpleOffer> offersToValidate;

    public ImportRunnable(int i, JTextArea log, ImportStatus status, String env, String urlTemplate,
            File fileChosenToImport, List<SimpleOffer> offersToValidate) {
        this.i = i;
        this.log = log;
        this.status = status;
        this.env = env;
        this.urlTemplate = urlTemplate;
        this.fileToImport = fileChosenToImport;
        this.offersToValidate = offersToValidate;
    }

    public void run() {
        try {
            // opóźnienie uruchomienia każdego wątku o 10 sekund, bo import dla kilku formularzy na raz się nie udaje
            Thread.sleep(i * 10000);
        } catch (InterruptedException e) {
        }

        status.addStatusAndUpdate(i, "sprawdzam folder w alfresco", log);
        // pobieram id folderu gdzie mają zostać zaimportowane oferty
        String destinationFolderId = getAlfrescoFolderIdByUrl(env);
        if (destinationFolderId == null) {
            status.addStatusAndUpdate(i, "błąd sprawdzania folderu z ofertami", log);
            return;
        }
        // sprawdzam czy folder przed importem jest pusty
        if (!isFolderEmpty(destinationFolderId)) {
            status.addStatusAndUpdate(i, "folder nie jest pusty, przerywam", log);
            return;
        }
        status.addStatusAndUpdate(i, "wysyłanie", log);
        // wysyłam plik do alfresco jako multipart POST
        String response = new HttpFileUploader().executeMultiPartRequest("http://admin:admin@" + env
                + "/alfresco/s/api/alfresco/exp_imp/import", fileToImport, "workspace://SpacesStore/"
                + destinationFolderId);
        if (!response.contains("\"success\": true")) {
            status.addStatusAndUpdate(i, "błąd wysyłki pliku zip", log);
            return;
        }
        status.addStatusAndUpdate(i, "wysłano " + offersToValidate.size() + " ofert", log);

        status.addStatusAndUpdate(i, "weryfikacja importu", log);
        // waliduję poprawność importu
        boolean importCorrect = false;
        int verifyAttempts = 0;
        while (!importCorrect && verifyAttempts < 10) {
            // czekam niech Alfresco przetrawi zaimportowane oferty
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            importCorrect = new ImportValidator().isImportCorrect(env, offersToValidate, destinationFolderId);
            if (importCorrect) {
                status.addStatusAndUpdate(i, "OK (" + offersToValidate.size() + " ofert)", log);
            } else {
                status.addShortStatusAndUpdate(i, ".", log);
                verifyAttempts++;
            }
        }
        if (!importCorrect) {
            status.addStatusAndUpdate(i, "brakuje ofert", log);
        }
    }

    /**
     * Sprawdza czy folder w Alfresco jest pusty.
     * 
     * @param destinationFolderId
     *            id folderu do sprawdzenia
     * @return
     */
    private boolean isFolderEmpty(String destinationFolderId) {
        String completeURL = String.format(urlTemplate, env, AlfrescoLogin.getTicket(env));
        try {
            Document doc = Jsoup.connect(completeURL).parser(Parser.xmlParser()).get();
            Elements children = doc.select("entry");
            if (children.isEmpty()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Zamienia URL folderu przekazany w polu "urlTemplate" na jego identyfikator alfresco.
     * 
     * @param env
     *            środowisko
     * @return
     */
    private String getAlfrescoFolderIdByUrl(String env) {
        String completeURL = String.format(urlTemplate, env, AlfrescoLogin.getTicket(env));
        completeURL = completeURL.replaceAll(" ", "%20");
        try {
            Document doc = null;
            Connection conn = Jsoup.connect(completeURL);
            conn.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .timeout(20000)
                .followRedirects(true)
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "pl-PL,pl;q=0.8,en-US;q=0.6,en;q=0.4")
                .header("Connection", "keep-alive")
                .parser(Parser.xmlParser());
            
            doc = conn.get();
            
            if (doc != null) {                
                Element idElement = doc.select("id").first();
                if (idElement != null) {
                    // zamieniam "urn:uuid:05dfeb0f-5728-4283-9d20-b034a5bdf225-children" na
                    // "05dfeb0f-5728-4283-9d20-b034a5bdf225"
                    return idElement.text().replace("urn:uuid:", "").replace("-children", "");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
