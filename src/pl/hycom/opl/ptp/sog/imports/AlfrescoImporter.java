package pl.hycom.opl.ptp.sog.imports;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTextArea;

import pl.hycom.opl.ptp.sog.model.FormId;
import pl.hycom.opl.ptp.sog.model.ParsedOffersPack;
import pl.hycom.opl.ptp.sog.model.SimpleOffer;

/**
 * Główny komponent startujący import ofert do Alfresco.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class AlfrescoImporter {
    /*- POPRAWNE URLe
     */
     public final String neoInsFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Neostrada/konfiguracja ofert/children?alf_ticket=%s";
     public final String neoModFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Neostrada mod/konfiguracja ofert/children?alf_ticket=%s";
     public final String neoInsBizFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Neostrada biznes/konfiguracja ofert/children?alf_ticket=%s";
     public final String neoModBizFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Neostrada mod biznes/konfiguracja ofert/children?alf_ticket=%s";
     public final String trplInsFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Funpack/konfiguracja ofert/children?alf_ticket=%s";
     public final String trplModFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/zamówienia/Proste zamówienia/Prosta oferta Funpack mod/konfiguracja ofert/children?alf_ticket=%s";

    // TYMCZASOWE URLe do pobrania dzieci folderu po nazwach folderów bo prowadzą do TP Portal/IE6 Info/... !!!!!!
//    public final String neoInsFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Neostrada/konfiguracja ofert/children?alf_ticket=%s";
//    public final String neoModFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Neostrada mod/konfiguracja ofert/children?alf_ticket=%s";
//    public final String neoInsBizFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Neostrada biznes/konfiguracja ofert/children?alf_ticket=%s";
//    public final String neoModBizFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Neostrada mod biznes/konfiguracja ofert/children?alf_ticket=%s";
//    public final String trplInsFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Funpack/konfiguracja ofert/children?alf_ticket=%s";
//    public final String trplModFolderUrl = "http://%s/alfresco/service/cmis/p/Sites/orangetp/documentLibrary/TP Portal/IE6 info/zamówienia/Proste zamówienia/Prosta oferta Funpack mod/konfiguracja ofert/children?alf_ticket=%s";

    private ImportStatus status = new ImportStatus();

    public void importOffers(String env, ParsedOffersPack parsedOffersPack, JTextArea log) {
        // resetuję status importu
        status.resetStatus();

        // mapowanie formId na właściwe URLe
        LinkedHashMap<String, String> formIdsToUrls = new LinkedHashMap<String, String>();
        formIdsToUrls.put(FormId.NEO_INS.getValue(), neoInsFolderUrl);
        formIdsToUrls.put(FormId.NEO_MOD.getValue(), neoModFolderUrl);
//        formIdsToUrls.put(FormId.NEO_INS_BIZ.getValue(), neoInsBizFolderUrl);
//        formIdsToUrls.put(FormId.NEO_MOD_BIZ.getValue(), neoModBizFolderUrl);
        formIdsToUrls.put(FormId.TRPL_INS.getValue(), trplInsFolderUrl);
        formIdsToUrls.put(FormId.TRPL_MOD.getValue(), trplModFolderUrl);

        // mapowanie formId na oferty
        Map<String, List<SimpleOffer>> formIdsToOffers = new HashMap<String, List<SimpleOffer>>();
        formIdsToOffers.put(FormId.NEO_INS.getValue(), parsedOffersPack.getNeoInsOffers());
        formIdsToOffers.put(FormId.NEO_MOD.getValue(), parsedOffersPack.getNeoModOffers());
//        formIdsToOffers.put(FormId.NEO_INS_BIZ.getValue(), parsedOffersPack.getNeoInsBizOffers());
//        formIdsToOffers.put(FormId.NEO_MOD_BIZ.getValue(), parsedOffersPack.getNeoModBizOffers());
        formIdsToOffers.put(FormId.TRPL_INS.getValue(), parsedOffersPack.getTrplInsOffers());
        formIdsToOffers.put(FormId.TRPL_MOD.getValue(), parsedOffersPack.getTrplModOffers());

        int i = 0;
        // ta pętla wykonuje się raz dla każdego z formularzy i dzięki powyższym mapom znany jest URL do każdego
        // formularza oraz oferty, które powinny się tam znajdować
        for (Entry<String, String> entry : formIdsToUrls.entrySet()) {
            // wyszukuję na dysku plik ZIP do wysyłki (zapisany wcześniej w %TEMP%)
            File offersFolder = new File(System.getProperty("java.io.tmpdir") + "/oferty/");
            File[] files = offersFolder.listFiles();
            File fileChosenToImport = null;
            if (files != null) {
                for (File file : files) {
                    // przykładowo dla neo-ins szukam pliku o nazwie zaczynającej się od "neo-ins_"
                    if (file.getName().startsWith(entry.getKey() + "_")) {
                        System.out.println("dla " + entry.getKey() + " wysyłam plik " + file.getAbsolutePath());
                        fileChosenToImport = file;
                        break;
                    }
                }
            }
            if (fileChosenToImport != null) {
                List<SimpleOffer> offersToValidate = formIdsToOffers.get(entry.getKey());
                if (!offersToValidate.isEmpty()) {
                    new Thread(new ImportRunnable(i++, log, status, env, entry.getValue(), fileChosenToImport,
                            offersToValidate)).start();
                }
            }
        }
    }

}
