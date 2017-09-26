package pl.hycom.opl.ptp.sog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.hycom.opl.ptp.sog.model.MainService;
import pl.hycom.opl.ptp.sog.model.ParsedOffersPack;
import pl.hycom.opl.ptp.sog.model.Process;
import pl.hycom.opl.ptp.sog.model.SimpleOffer;
import pl.hycom.opl.ptp.sog.util.CSVHelper;
import pl.hycom.opl.ptp.sog.util.XmlAndZipWriter;

/**
 * Generuje oferty SimpleOffer z pliku CSV i zapisuje w katalogu tymczasowym.
 * 
 * @author krystian.gorecki@hycom.pl
 */

public class SimpleOfferGenerator {
    ParsedOffersPack parsedOffersPack = new ParsedOffersPack();
    /** Lista wszystkich ofert załadowanych z pliku CSV. */
    private List<SimpleOffer> allOffers;
    List<SimpleOffer> neoInsOffers;
    List<SimpleOffer> neoModOffers;
//    List<SimpleOffer> neoInsBizOffers;
//    List<SimpleOffer> neoModBizOffers;
    List<SimpleOffer> trplInsOffers;
    List<SimpleOffer> trplModOffers;

    public ParsedOffersPack execute(String csvFilePath) {
        if (csvFilePath == null) {
            csvFilePath = "KonfiguracjaOfert_NP.csv";
        }
        logDebug(new File(csvFilePath).getAbsolutePath());
        parsedOffersPack = new ParsedOffersPack();
        allOffers = parseCSVFileToSimpleOffers(csvFilePath);
        parsedOffersPack.setAllOffers(allOffers);
        logDebug("Parsed " + allOffers.size() + " offers.");
        if (!allOffers.isEmpty()) {
            neoInsOffers = filterOffers(allOffers, MainService.NEOI0010, Process.AKWIZYCJA, "ind");
            neoModOffers = filterOffers(allOffers, MainService.NEOI0010, Process.UTRZYMANIE, "ind");
//            neoInsBizOffers = filterOffers(allOffers, MainService.NEOI0010, Process.AKWIZYCJA, "biz");
//            neoModBizOffers = filterOffers(allOffers, MainService.TRPL0010, Process.UTRZYMANIE, "biz");
            trplInsOffers = filterOffers(allOffers, MainService.TRPL0010, Process.AKWIZYCJA, "ind");
            trplModOffers = filterOffers(allOffers, MainService.TRPL0010, Process.UTRZYMANIE, "ind");
            parsedOffersPack.setNeoInsOffers(neoInsOffers);
            parsedOffersPack.setNeoModOffers(neoModOffers);
//            parsedOffersPack.setNeoInsBizOffers(neoInsBizOffers);
//            parsedOffersPack.setNeoModBizOffers(neoModBizOffers);
            parsedOffersPack.setTrplInsOffers(trplInsOffers);
            parsedOffersPack.setTrplModOffers(trplModOffers);
            System.out.println(parsedOffersPack.toString());
            new XmlAndZipWriter().saveOffersToZIPFiles(parsedOffersPack, null);
        }
        return parsedOffersPack;
    }

    /**
     * Wybiera z listy ofert tylko te o podanej usłudze głównej, określonym procesie i typie użytkownika. Oryginalna
     * lista zostaje nienaruszona.
     * 
     * @param offers
     *            lista ofert
     * @param mainService
     *            usługa główna
     * @param process
     *            typ procesu
     * @param userType
     *            typ użytkownika
     * @return przefiltrowana lista
     */
    private static List<SimpleOffer> filterOffers(List<SimpleOffer> offers, MainService mainService, Process process,
            String userType) {
        List<SimpleOffer> result = new ArrayList<SimpleOffer>();
        for (SimpleOffer offer : offers) {
            if (process.equals(offer.getProcess()) && mainService.equals(offer.getMainService())) {
                // TODO dodać warunek dla ind i biz jak w excelu dojdzie kolumna z typem usera
                result.add(offer);
            }
        }
        return result;
    }

    /**
     * Zamienia plik CSV na listę ofert.
     * 
     * @param filePath
     *            ścieżka do pliku CSV
     * @return lista ofert
     */
    private List<SimpleOffer> parseCSVFileToSimpleOffers(String filePath) {
        CSVHelper csvHelper = new CSVHelper();
        List<SimpleOffer> resultList = new ArrayList<SimpleOffer>();
        try {
            BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            // pomijam linię z nagłówkiem
            String line = fr.readLine();
            int i = 0;
            while ((line = fr.readLine()) != null) {
                List<String> parsedLine = csvHelper.parseLine(line);
                if (parsedLine != null) {
                    SimpleOffer s = new SimpleOffer(i++, parsedLine);
                    resultList.add(s);
                    logDebug(s.toString());
                }
            }
            fr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        Collections.sort(resultList, new Comparator<SimpleOffer>() {

            public int compare(SimpleOffer o1, SimpleOffer o2) {
                if (o1.getPromotion().equals(o2.getPromotion())) {
                    if (o1.getSpeed().equals(o2.getSpeed())) {
                        if (o1.getPriority().equals(o2.getPriority())) {
                            return o1.getPriorityForCustomOffer().compareTo(o2.getPriorityForCustomOffer());
                        }
                        
                        return o1.getPriority().compareTo(o2.getPriority());
                    }
                    
                    return o1.getSpeed().compareTo(o2.getSpeed());
                }
                
                return o1.getPromotion().compareTo(o2.getPromotion());
            }
        });
        
        return resultList;
    }

    private static void logDebug(String msg) {
        System.out.println(msg);
    }

}
