package pl.hycom.opl.ptp.sog.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reprezentuje prostą ofertę.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class SimpleOffer {
    private String name; // generowane
    private String priority;
    private String priorityForCustomOffer;
    private String defaultOffer;
    private MainService mainService;
    private Process process;
    private String technology;
    private String market;
    private String speed;
    private String promotion;
    private String gadget;
    private String tvPackage;
    private String ncplusPackages;
    private String ntoPackage;
    private String additionalProducts;
    private String channelsNumber; // brakuje (mapowanie skonfigurowane w tej aplikacji)
    private String ribbon;// brakuje (nieużywane)
    private String availableGadgets; // brakuje (mapowanie skonfigurowane w tej aplikacji)
    private String promoGroupNames; // brakuje (mapowanie skonfigurowane w tej aplikacji)
    private String tvPackageDescription; // brakuje (nieużywane)
    private String modems; // brakuje (mapowanie skonfigurowane w tej aplikacji)
    public static Map<String, String> speedMap;

    static {
        speedMap = new HashMap<String, String>();
        speedMap.put("NEOI0010:10", "NEOIAC14");
        speedMap.put("NEOI0010:20", "NEOIAC10");
        speedMap.put("NEOI0010:80", "NEOIAC16");
        speedMap.put("NEOI0010:100", "NEOIAC17");
        speedMap.put("NEOI0010:300", "NEOIAC13");
        speedMap.put("NEOI0010:600", "NEOIAC18");
        speedMap.put("TRPL0010:10", "TRPL005P");
        speedMap.put("TRPL0010:20", "TRPL005R");
        speedMap.put("TRPL0010:80", "TRPL005W");
        speedMap.put("TRPL0010:100", "TRPL005X");
        speedMap.put("TRPL0010:300", "TRPL005Y");
        speedMap.put("TRPL0010:600", "TRPL005Z");
    }
    
    public static Map<String, Integer> channelsNumberMap;
    
    /**
        Optymalny   98 (41 HD)
        Bogaty      142 (69 HD)
        Maksymalny  171 (88 HD)     
     */
    static {
        channelsNumberMap = new HashMap<String, Integer>();
        channelsNumberMap.put("VIDPTPBSDTH", 95);
        channelsNumberMap.put("VIDPTNSECP", 95);
        channelsNumberMap.put("VIDPTNSECT", 95);
        channelsNumberMap.put("VIDPTPBSIP", 98);        
        channelsNumberMap.put("VIDPTPM", 142);
        channelsNumberMap.put("VIDPTPXL", 171);
    }

    public static Map<String, String> modemsMap;
    
    static {
        modemsMap = new HashMap<String, String>();
        // 10M
        //modemsMap.put("NEOIAC14", "!NEOIWOFB10-NEO_GENERIC_NO_PROMOTION,DEVSLMODW8950N-NEO_GENERIC_NO_PROMOTION,DEVSLMODNON_PTP-NEO_GENERIC_NO_PROMOTION");
        modemsMap.put("NEOIAC14", "!NEOIWOFB10-NEO_GENERIC_NO_PROMOTION,DEVRTLBOX20S-NEO_GENERIC_NO_PROMOTION,DEVSLMODNON_PTP-NEO_GENERIC_NO_PROMOTION");
        // 20M
        //modemsMap.put("NEOIAC10", "!NEOIWOFB10-NEO_GENERIC_NO_PROMOTION,DEVSLMODW8950N-NEO_GENERIC_NO_PROMOTION,DEVSLMODNON_PTP-NEO_GENERIC_NO_PROMOTION");
        modemsMap.put("NEOIAC10", "!NEOIWOFB10-NEO_GENERIC_NO_PROMOTION,DEVRTLBOX20S-NEO_GENERIC_NO_PROMOTION,DEVSLMODNON_PTP-NEO_GENERIC_NO_PROMOTION");
        // 80M
        modemsMap.put("NEOIAC16", "!NEOIWOFB1-NEO_GENERIC_NO_PROMOTION");
        // 100M
        modemsMap.put("NEOIAC17", "!DEVRTLBOXOFB30-NEO_FTTH_GENERIC_NO_PROMOTION");
        // 300M
        modemsMap.put("NEOIAC13", "!DEVRTLBOXOFB30-NEO_FTTH_GENERIC_NO_PROMOTION");
        // 600M
        modemsMap.put("NEOIAC18", "!DEVRTLBOXOFB30-NEO_FTTH_GENERIC_NO_PROMOTION");
    }

    public static Map<String, String> pgnMap;
    
    static {
        pgnMap = new HashMap<String, String>();
        pgnMap.put("PTRPLPXDSL", "TRPL_GENERIC_NO_PROMOTION,PTRPLPXDSL,PVIDPTPWTTB");
        pgnMap.put("PTRPLPFTTH", "TRPL_FTTH_GENERIC_NO_PROMOTION,PTRPLPXFTTH,PVIDPTPWTTB");
        pgnMap.put("PNEOIPXDSL", "NEO_GENERIC_NO_PROMOTION,PNEOIPXDSL");
        pgnMap.put("PNEOIPFTTH", "NEO_FTTH_GENERIC_NO_PROMOTION,PNEOIPXFTTH");
        
        pgnMap.put("AKWIZYCJA-12", "PVIDPTNRCAN");
        pgnMap.put("AKWIZYCJA-24", "PVIDPTNRCANUR");
        pgnMap.put("UTRZYMANIE-12", "PVIDPTNRCAN");
        //pgnMap.put("UTRZYMANIE-24", "PVIDPTNRCANW");
        pgnMap.put("UTRZYMANIE-24", "PVIDPTNRCANUR");
    }

    /**
     * Zamienia linię CSV na prostą ofertę.
     * @param i 
     * 
     * @param parsedLine
     *            linia z pliku CSV rozdzialana średnikami
     */
    public SimpleOffer(int i, List<String> parsedLine) {
        try {
            String priorityRaw = parsedLine.get(0);
            priority = priorityRaw.equalsIgnoreCase("n/d") ? "1000" : priorityRaw;
            String priorityForCustomOfferRaw = parsedLine.get(1);
            priorityForCustomOffer = priorityForCustomOfferRaw.equalsIgnoreCase("n/d") ? "1000" : priorityForCustomOfferRaw;
            String defaultRaw = parsedLine.get(2);
            defaultOffer = defaultRaw.equalsIgnoreCase("TAK") ? "true" : defaultRaw.equalsIgnoreCase("NIE") ? "false" : "BLAD";
            mainService = MainService.byValue(parsedLine.get(3));
            if (mainService == null) {
                logDebug("Invalid main service id: '" + parsedLine.get(3) + "' in line:\n" + parsedLine);
            }
            String processRaw = parsedLine.get(4);
            process = processRaw.equals("akwizycja") ? Process.AKWIZYCJA : processRaw.equals("utrzymanie") ? Process.UTRZYMANIE : null;
            technology = parsedLine.get(5);
            String marketRaw = parsedLine.get(6);
            market = marketRaw.equalsIgnoreCase("reg") ? "1" : marketRaw.equalsIgnoreCase("oba") ? "2" : marketRaw
                    .equalsIgnoreCase("dereg") ? "0" : "BLAD";
            String speedInMbps = parsedLine.get(7);
            speed = mapMbpsSpeedToCKPSpeed(speedInMbps, mainService);
            if (speed == null) {
                System.out.println("Speed CKP id mapping not found for " + mainService + ":" + speedInMbps);
            }
            // 8 to 12 m-cy albo 24 m-ce
            promotion = parsedLine.get(9); // końcówka 12 albo 24
            gadget = parsedLine.get(10);
            if (null != gadget && gadget.equals("n/d")) {
                gadget = "";
            }
            availableGadgets = gadget;
            tvPackageDescription = "";
            ribbon = "";
            String tvPackageRaw = parsedLine.get(11).replaceAll(" ", "");
            tvPackage = tvPackageRaw.equalsIgnoreCase("n/d") ? "" : tvPackageRaw;
            String ncPlusPackagesRaw = parsedLine.get(12).replaceAll(" ", "").replaceAll(",,", ",");
            ncplusPackages = ncPlusPackagesRaw.equalsIgnoreCase("n/d") ? "" : ncPlusPackagesRaw;
            promoGroupNames = handlePromoGroupNames(mainService, process, promotion, speed);
            modems = null == speed ? "" : handleModems(speed);
            //channelsNumber = "".equals(tvPackage) ? "<view:value view:isNull=\"true\"></view:value>" : handleChannelsNumber(tvPackage); //parsedLine.get(15) == null || parsedLine.get(15).isEmpty() ? "<view:value view:isNull=\"true\"></view:value>" : parsedLine.get(15).trim();
            channelsNumber = "<view:value view:isNull=\"true\"></view:value>";

            if (parsedLine.size() > 13) {
                String ntoPackageRaw = parsedLine.get(13);
                ntoPackage = ntoPackageRaw.equalsIgnoreCase("n/d") ? "" : ntoPackageRaw;
                additionalProducts = parsedLine.get(14).replaceAll(" ", "");
            }
            additionalProducts = handleAdditionals(additionalProducts == null ? "" : additionalProducts, mainService, speed, promotion,
                    process, tvPackage);

            String withLine = isOfferWithLineString();

            name = speed + " (" + speedInMbps + "Mb/s - " + promotion + " - " + withLine + " - " + formatMarket() + " - P"
                    + formatPriority(priority) + "/" + formatPriority(priorityForCustomOffer) + " - " + gadget + ")";
        } catch (Exception e) {
            System.err.println("[" + i + "] EEEeee! Oferta: " + Arrays.toString(parsedLine.toArray()) + ", błąd: " + e);
        }
    }

    /**
     * @param promotion 
     * @param speed 
     * @param additionalProducts 
     * @param process 
     * @param tvPackage 
     * @return
     */
    private String handleAdditionals(String additionalProducts, MainService mainService, String speed, String promotion, Process process, String tvPackage) {
        StringBuilder additionals = new StringBuilder();

        // *ZMKT i *GEOEDF
        switch (mainService) {
            case NEOI0010:
                additionals.append("NEOZMKT").append('-').append(promotion);
                additionals.append(',').append("NEOGEOEDF").append('-').append(promotion);
                
                // NEOZNWUU-NEO_GENERIC_NO_PROMOTION
                if ("NEOIAC14".equals(speed) || "NEOIAC10".equals(speed) || "NEOIAC16".equals(speed)) {
                    appendComma(additionals);            
                    additionals.append("NEOZNWUU-NEO_GENERIC_NO_PROMOTION");
                }
                
                // NEOILACZ i NEOIINSTNN
                if (additionalProducts.contains("NEOILACZ")) {
                    appendComma(additionals);            
                    additionals.append("NEOILACZ-NEO_GENERIC_NO_PROMOTION");            
                    appendComma(additionals);            
                    additionals.append("NEOIINSTNN").append('-').append(promotion);            
                } else if (!("NEOIAC13".equals(speed) || "NEOIAC17".equals(speed) || "NEOIAC18".equals(speed)) && process == Process.AKWIZYCJA) {
                    // NEOIINST
                    appendComma(additionals);            
                    additionals.append("NEOIINST").append('-').append(promotion);           
                }
                
                // NEOIINSTNN
                if ("NEOIAC13".equals(speed) || "NEOIAC17".equals(speed) || "NEOIAC18".equals(speed)) {
                    appendComma(additionals);            
                    additionals.append("NEOIINSTNN").append('-').append(promotion);            
                }
                
                break;
            case TRPL0010:
                additionals.append("TRPLZMKT").append('-').append(promotion);
                additionals.append(',').append("TRPLGEOEDF").append('-').append(promotion);

                // NEOILACZ
                if (additionalProducts.contains("NEOILACZ")) {
                    appendComma(additionals);            
                    additionals.append("NEOILACZ-TRPL_GENERIC_NO_PROMOTION");
                }

                // DEVRTSTBWHD80-TRPL_GENERIC_NO_PROMOTION
                /*if ("TRPL005P".equals(speed)) {
                    appendComma(additionals);            
                    additionals.append("DEVRTSTBWHD80-TRPL_GENERIC_NO_PROMOTION");
                }*/
                
                // NEOI3PINST-TRPL_GENERIC_NO_PROMOTION
                if ("TRPL005R".equals(speed) || "TRPL005W".equals(speed)) {
                    appendComma(additionals);            
                    additionals.append("NEOI3PINST-TRPL_GENERIC_NO_PROMOTION");
                }
                
                // NEOI3PINST-TRPL_FTTH_GENERIC_NO_PROMOTION
                if ("TRPL005X".equals(speed) || "TRPL005Y".equals(speed) || "TRPL005Z".equals(speed)) {
                    appendComma(additionals);            
                    additionals.append("NEOI3PINST-TRPL_FTTH_GENERIC_NO_PROMOTION");
                    // TRPLINSTFTTH
                    appendComma(additionals);            
                    additionals.append("TRPLINSTFTTH").append('-').append(promotion);
                    // DEVRTSTBICU100-PTV_GENERIC_NO_PROMOTION
                    appendComma(additionals);            
                    additionals.append("DEVRTSTBICU100-PTV_GENERIC_NO_PROMOTION");
                    // NEOINETACFTTH-TRPL_FTTH_GENERIC_NO_PROMOTION
                    appendComma(additionals);            
                    additionals.append("NEOINETACFTTH-TRPL_FTTH_GENERIC_NO_PROMOTION");                    
                } else {
                    // TRPLINST
                	if(process == Process.AKWIZYCJA){
	                    appendComma(additionals);
	                    additionals.append("TRPLINST").append('-').append(promotion);
	                    //DEVRTSTBWHD80-TRPL_GENERIC_NO_PROMOTION
	                	appendComma(additionals);
	                    //additionals.append("DEVRTSTBWHD80-TRPL_GENERIC_NO_PROMOTION");
	                	additionals.append("DEVRTSTBWHD80-PTV_GENERIC_NO_PROMOTION");
                	}
                }
                
//                if (process == Process.AKWIZYCJA) {                    
//                    String tvPackageId = "";
//                    String tvPackagePromoId = "";
//                    if (tvPackage != null && !tvPackage.isEmpty()) {
//                        tvPackageId = tvPackage.substring(0, tvPackage.indexOf('-'));
//                        tvPackagePromoId = tvPackage.substring(tvPackage.indexOf('-') + 1, tvPackage.length());
//                    }
//                    
//                    // VIDPOAKTTRY dla bogatych i maksymalnych
//                    List<String> vidpoakttryTvPackages = Arrays.asList(new String [] {"VIDPTPM", "VIDPTPXL"});
//                    if (!tvPackageId.isEmpty() && !tvPackagePromoId.isEmpty() && vidpoakttryTvPackages.contains(tvPackageId)) {
//                        appendComma(additionals);
//                        additionals.append("VIDPOAKTTRY").append('-').append(tvPackagePromoId);
//                    }
//                }
                
                break;
            default:
        }
        

        // --VOIPSOFTZNWUU
        
        return additionals.toString();
    }

    /**
     * @param additionals
     */
    private void appendComma(StringBuilder additionals) {
        if (additionals.length() > 0) {
            additionals.append(',');
        }
    }

    /**
     * @param mainService
     * @param process
     * @param technology
     * @param promotion
     * @return
     */
    private String handlePromoGroupNames(MainService mainService, Process process, String promotion, String speed) {
        StringBuilder pgn = new StringBuilder();
        if ("TRPL005P".equals(speed)) {
            pgn.append("PTV_NO_PROMOTION");
        } else if (mainService.equals(MainService.TRPL0010)) {
            appendComma(pgn);
            pgn.append("PVIDPTPWTTB");            
        }
        
        if (MainService.TRPL0010.equals(mainService)) {
            appendComma(pgn);
            pgn.append("PTV_GENERIC_NO_PROMOTION");
            
            String processPeriodKey = process.name() + "-" + promotion.substring(promotion.length() - 2, promotion.length());
            if (pgnMap.containsKey(processPeriodKey)) {
                appendComma(pgn);
                pgn.append(pgnMap.get(processPeriodKey));
            }
        }
        
        String noPeriodPromoKey = promotion.substring(0, promotion.length() -2);
        if (pgnMap.containsKey(noPeriodPromoKey)) {
            appendComma(pgn);
            pgn.append(pgnMap.get(noPeriodPromoKey));
        }
        
        return pgn.toString();
    }

    /**
     * @param speed
     * @return
     */
    private String handleModems(String speed) {
        if (modemsMap.containsKey(speed)) {
            return modemsMap.get(speed);                    
        }
        return "";
    }

    /**
     * @param tvPackage
     * @return
     */
    private String handleChannelsNumber(String tvPackage) { 
        String tvPackageKey = tvPackage.substring(0, tvPackage.indexOf('-'));
        return String.valueOf(channelsNumberMap.get(tvPackageKey)) == null ? "<view:value view:isNull=\"true\"></view:value>" : String
                .valueOf(channelsNumberMap.get(tvPackageKey));
    }

    private String isOfferWithLineString() {
        // jeśli oferta ma w sobie produkt z łączem to jest przeznaczona dla
        // klientów nieposiadających łącza.
        return additionalProducts.contains("NEOILACZ") ? "nie mam linii" : "mam linię";
    }

    private String formatMarket() {
        if (market.equals("1")) {
            return "regulowany";
        } else if (market.equals("1")) {
            return "deregulowany";
        } else {
            return "oba rynki";
        }
    }

    private String formatPriority(String pri) {
        if (pri.equals("1000")) {
            return "nd";
        } else {
            return pri;
        }
    }

    /**
     * Zamienia prędkość w Mb/s na jej identyfikator z CKP. Na przykład zamienia "80" na NEOIAC14
     * 
     * @param speedInMbps
     *            prędkośc w Mbps (sama liczba bez jednostki)
     * @param mainServiceId
     * @return
     */
    private String mapMbpsSpeedToCKPSpeed(String speedInMbps, MainService mainServiceId) {
        return speedMap.get(mainServiceId.name() + ":" + speedInMbps);
    }

    public String getPriority() {
        return priority;
    }

    public String getPriorityForCustomOffer() {
        return priorityForCustomOffer;
    }

    public String getDefaultOffer() {
        return defaultOffer;
    }

    public MainService getMainService() {
        return mainService;
    }

    public Process getProcess() {
        return process;
    }

    public String getTechnology() {
        return technology;
    }

    public String getMarket() {
        return market;
    }

    public String getSpeed() {
        return speed;
    }

    public String getPromotion() {
        return promotion;
    }

    public String getGadget() {
        return gadget;
    }

    public String getTvPackage() {
        return tvPackage;
    }

    public String getNcplusPackages() {
        return ncplusPackages;
    }

    public String getNtoPackage() {
        return ntoPackage;
    }

    public String getAdditionalProducts() {
        return additionalProducts;
    }

    private void logDebug(String msg) {
        System.out.println(msg);
    }

    @Override
    public String toString() {
        return "SimpleOffer [priority=" + priority + ", priorityForCustomOffer=" + priorityForCustomOffer + ", defaultOffer="
                + defaultOffer + ", mainService=" + mainService + ", process=" + process + ", technology=" + technology + ", market="
                + market + ", speed=" + speed + ", promotion=" + promotion + ", gadget=" + gadget + ", tvPackage=" + tvPackage
                + ", ncplusPackages=" + ncplusPackages + ", ntoPackage=" + ntoPackage + ", additionalProducts=" + additionalProducts
                + ", promoGroupNames=" + promoGroupNames + ", modems=" + modems + "]";
    }

    public String getName() {
        return name;
    }

    public String getChannelsNumber() {
        return channelsNumber;
    }

    public String getRibbon() {
        return ribbon;
    }

    public String getAvailableGadgets() {
        return availableGadgets;
    }

    public String getPromoGroupNames() {
        return promoGroupNames;
    }

    public String getTvPackageDescription() {
        return tvPackageDescription;
    }

    public String getModems() {
        return modems;
    }

}
