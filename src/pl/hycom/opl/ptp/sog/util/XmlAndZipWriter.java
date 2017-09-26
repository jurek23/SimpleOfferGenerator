package pl.hycom.opl.ptp.sog.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import pl.hycom.opl.ptp.sog.model.FormId;
import pl.hycom.opl.ptp.sog.model.ParsedOffersPack;
import pl.hycom.opl.ptp.sog.model.SimpleOffer;

/**
 * Zamienia proste oferty na XML, który da sie zaimportować do aplfresco. Kompresuje XML do pliku ZIP. Zapisuje na dysku
 * w folderze %TEMP%/oferty/.
 * 
 * @author krystian.gorecki@hycom.pl
 *
 */
public class XmlAndZipWriter {

    private final String xmlFilenameSuffix = "_offers.xml";
    private final String zipFilenameSuffix = "_offers.zip";
    private final String xmlTemplateHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n <view:view xmlns:view=\"http://www.alfresco.org/view/repository/1.0\">\n      <view:metadata>\n     <view:exportBy>System</view:exportBy>\n     <view:exportDate>2017-03-18T00:19:35.625+01:00</view:exportDate>\n      <view:exporterVersion>4.2.0 (4576)</view:exporterVersion>\n     <view:exportOf>/app:company_home/st:sites/cm:orangetp/cm:documentLibrary/cm:TP_x0020_Portal</view:exportOf>\n     </view:metadata>";
    private final String xmlTemplateMain = "\n    <tpsimpleoffer:tp_simple_offer xmlns=\"\" xmlns:onpglorious=\"http://www.amg.net.pl/isana/alfresco/model/onpglorious\" xmlns:nt=\"http://www.jcp.org/jcr/nt/1.0\" xmlns:gd=\"http://www.alfresco.org/model/googledocs/1.0\" xmlns:tpparameter=\"http://www.amg.net.pl/alfresco/model/tpparameter\" xmlns:tpneoperiod=\"http://www.amg.net.pl/alfresco/model/tpneoperiod\" xmlns:fm=\"http://www.alfresco.org/model/forum/1.0\" xmlns:tparticlewithouttiny=\"http://www.amg.net.pl/isana/alfresco/model/tparticlewithouttiny\" xmlns:tplink=\"http://www.amg.net.pl/isana/alfresco/model/tplink\" xmlns:tpneoadditionalservicefolder=\"http://www.amg.net.pl/alfresco/model/tpneoadditionalservicefolder\" xmlns:tphtmlapplication=\"http://www.amg.net.pl/alfresco/model/tphtmlapplication\" xmlns:tpverconfig=\"http://www.amg.net.pl/salsa/alfresco/model/tpverconfig\" xmlns:youtube=\"http://www.alfresco.org/model/publishing/youtube/1.0\" xmlns:wcm=\"http://www.alfresco.org/model/wcmmodel/1.0\" xmlns:tpvariable=\"http://www.amg.net.pl/isana/alfresco/model/tpvariable\" xmlns:tpsimpleofferkpname=\"http://www.amg.net.pl/isana/alfresco/model/tpsimpleofferkpname\" xmlns:tpadvisorofferabody=\"http://www.amg.net.pl/salsa/alfresco/model/tpadvisorofferabody\" xmlns:exif=\"http://www.alfresco.org/model/exif/1.0\" xmlns:tporderable=\"http://www.amg.net.pl/isana/alfresco/model/tporderable\" xmlns:alfcmis=\"http://www.alfresco.org/model/cmis/1.0/alfcmis\" xmlns:tpproductgroupfolder=\"http://www.amg.net.pl/alfresco/model/tpproductgroupfolder\" xmlns:tpverstatusmapping=\"http://www.amg.net.pl/isana/alfresco/model/tpverstatusmapping\" xmlns:tpwttag=\"http://www.amg.net.pl/isana/alfresco/model/tpwttag\" xmlns:tpfaxfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpfaxfolder\" xmlns:wca=\"http://www.alfresco.org/model/wcmappmodel/1.0\" xmlns:inwf=\"http://www.alfresco.org/model/workflow/invite/nominated/1.0\" xmlns:flickr=\"http://www.alfresco.org/model/publishing/flickr/1.0\" xmlns:tpsimpleoffer=\"http://www.amg.net.pl/isana/alfresco/model/tpsimpleoffer\" xmlns:tppicture=\"http://www.amg.net.pl/isana/alfresco/model/tppicture\" xmlns:tpsectionfolder=\"http://www.amg.net.pl/alfresco/model/tpsectionfolder\" xmlns:tpsku=\"http://www.amg.net.pl/salsa/alfresco/model/tpsku\" xmlns:tptariffplanfolder=\"http://www.amg.net.pl/alfresco/model/tptariffplanfolder\" xmlns:tpwirelessproductfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpwirelessproductfolder\" xmlns:tpcustomtext=\"http://www.amg.net.pl/isana/alfresco/model/tpcustomtext\" xmlns:ver2=\"http://www.alfresco.org/model/versionstore/2.0\" xmlns:view=\"http://www.alfresco.org/view/repository/1.0\" xmlns:imwf=\"http://www.alfresco.org/model/workflow/invite/moderated/1.0\" xmlns:tphelpfolder=\"http://www.amg.net.pl/isana/alfresco/model/tphelpfolder\" xmlns:tpadvisorofferanswerfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpadvisorofferanswerfolder\" xmlns:wf=\"http://www.alfresco.org/model/workflow/1.0\" xmlns:trx=\"http://www.alfresco.org/model/transfer/1.0\" xmlns:tpcalltype=\"http://www.amg.net.pl/alfresco/model/tpcalltype\" xmlns:tpfolder=\"http://www.amg.net.pl/isana/alfresco/model/tpfolder\" xmlns:tpproductattribute=\"http://www.amg.net.pl/alfresco/model/tpproductattribute\" xmlns:tpcfcparameterslist=\"http://www.amg.net.pl/alfresco/model/tpcfcparameterslist\" xmlns:tpportalurl=\"http://www.amg.net.pl/isana/alfresco/model/tpportalurl\" xmlns:sys=\"http://www.alfresco.org/model/system/1.0\" xmlns:lnk=\"http://www.alfresco.org/model/linksmodel/1.0\" xmlns:webdav=\"http://www.alfresco.org/model/webdav/1.0\" xmlns:ver=\"http://www.alfresco.org/model/versionstore/1.0\" xmlns:tpizisectionfolder=\"http://www.amg.net.pl/alfresco/model/tpizisectionfolder\" xmlns:tpspecialproductfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpspecialproductfolder\" xmlns:tpsite=\"http://www.amg.net.pl/isana/alfresco/model/tpsite\" xmlns:emailserver=\"http://www.alfresco.org/model/emailserver/1.0\" xmlns:tplinkssection=\"http://www.amg.net.pl/alfresco/model/tplinkssection\" xmlns:tpfoldersymlink=\"http://www.amg.net.pl/salsa/alfresco/model/tpfoldersymlink\" xmlns:tpneomodem=\"http://www.amg.net.pl/alfresco/model/tpneomodem\" xmlns:dl=\"http://www.alfresco.org/model/datalist/1.0\" xmlns:usr=\"http://www.alfresco.org/model/user/1.0\" xmlns:onpskin=\"http://www.amg.net.pl/isana/alfresco/model/onpskin\" xmlns:tpverquestion=\"http://www.amg.net.pl/salsa/alfresco/model/tpverquestion\" xmlns:app=\"http://www.alfresco.org/model/application/1.0\" xmlns:module=\"http://www.alfresco.org/system/modules/1.0\" xmlns:d=\"http://www.alfresco.org/model/dictionary/1.0\" xmlns:blg=\"http://www.alfresco.org/model/blogintegration/1.0\" xmlns:tpsymlinkmasterunpublished=\"http://www.amg.net.pl/salsa/alfresco/model/tpsymlinkmasterunpublished\" xmlns:linkedin=\"http://www.alfresco.org/model/publishing/linkedin/1.0\" xmlns:tpproductcategoryfolder=\"http://www.amg.net.pl/alfresco/model/tpproductcategoryfolder\" xmlns:tpneospeedfolder=\"http://www.amg.net.pl/alfresco/model/tpneospeedfolder\" xmlns:tporderdispatchconfiguration=\"http://www.amg.net.pl/alfresco/model/tporderdispatchconfiguration\" xmlns:tpquestion=\"http://www.amg.net.pl/salsa/alfresco/model/tpquestion\" xmlns:mix=\"http://www.jcp.org/jcr/mix/1.0\" xmlns:tpbanner=\"http://www.amg.net.pl/isana/alfresco/model/tpbanner\" xmlns:tpquestionnairefolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpquestionnairefolder\" xmlns:tpscript=\"http://www.amg.net.pl/isana/alfresco/model/tpscript\" xmlns:tpjob=\"http://www.amg.net.pl/salsa/alfresco/model/tpjob\" xmlns:tpbinarynotification=\"http://www.amg.net.pl/salsa/alfresco/model/tpbinarynotification\" xmlns:tpforbiddenword=\"http://www.amg.net.pl/salsa/alfresco/model/tpforbiddenword\" xmlns:tpshopapi=\"http://www.amg.net.pl/salsa/alfresco/model/tpshopapi\" xmlns:reg=\"http://www.alfresco.org/system/registry/1.0\" xmlns:wcmwf=\"http://www.alfresco.org/model/wcmworkflow/1.0\" xmlns:tpnewsletterfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpnewsletterfolder\" xmlns:tpfile=\"http://www.amg.net.pl/isana/alfresco/model/tpfile\" xmlns:tpproductother=\"http://www.amg.net.pl/salsa/alfresco/model/tpproductother\" xmlns:tparticle=\"http://www.amg.net.pl/isana/alfresco/model/tparticle\" xmlns:onpglorioustab=\"http://www.amg.net.pl/isana/alfresco/model/onpglorioustab\" xmlns:download=\"http://www.alfresco.org/model/download/1.0\" xmlns:twitter=\"http://www.alfresco.org/model/publishing/twitter/1.0\" xmlns:tpstatusmapping=\"http://www.amg.net.pl/salsa/alfresco/model/tpstatusmapping\" xmlns:facebook=\"http://www.alfresco.org/model/publishing/facebook/1.0\" xmlns:tpagreement=\"http://www.amg.net.pl/isana/alfresco/model/tpagreement\" xmlns:sv=\"http://www.jcp.org/jcr/sv/1.0\" xmlns:tpadvisorofferresultfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpadvisorofferresultfolder\" xmlns:st=\"http://www.alfresco.org/model/site/1.0\" xmlns:tpwireproductfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpwireproductfolder\" xmlns:tpcategoryfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpcategoryfolder\" xmlns:tpneopromotionfolder=\"http://www.amg.net.pl/alfresco/model/tpneopromotionfolder\" xmlns:tpmailingfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpmailingfolder\" xmlns:custom=\"custom.model\" xmlns:commonconstraint=\"http://www.amg.net.pl/common/alfresco/model/commonconstraint\" xmlns:qshare=\"http://www.alfresco.org/model/qshare/1.0\" xmlns:tpadvisorofferqbody=\"http://www.amg.net.pl/salsa/alfresco/model/tpadvisorofferqbody\" xmlns:tpproductfolder=\"http://www.amg.net.pl/alfresco/model/tpproductfolder\" xmlns:tpadvisorofferquestionfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpadvisorofferquestionfolder\" xmlns:tplaptopfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tplaptopfolder\" xmlns:pub=\"http://www.alfresco.org/model/publishing/1.0\" xmlns:tpformgroup=\"http://www.amg.net.pl/alfresco/model/tpformgroup\" xmlns:commoninternal=\"http://www.amg.net.pl/isana/alfresco/model/commoninternal\" xmlns:tpeditorpayment=\"http://www.amg.net.pl/salsa/alfresco/model/tpeditorpayment\" xmlns:tpgenericformfolder=\"http://www.amg.net.pl/alfresco/model/tpgenericformfolder\" xmlns:imap=\"http://www.alfresco.org/model/imap/1.0\" xmlns:cm=\"http://www.alfresco.org/model/content/1.0\" xmlns:commoncontainer=\"http://www.amg.net.pl/common/alfresco/model/commoncontainer\" xmlns:stcp=\"http://www.alfresco.org/model/sitecustomproperty/1.0\" xmlns:tpcaseelementfolder=\"http://www.amg.net.pl/alfresco/model/tpcaseelementfolder\" xmlns:tpiziformfolder=\"http://www.amg.net.pl/alfresco/model/tpiziformfolder\" xmlns:cmisext=\"http://www.alfresco.org/model/cmis/1.0/cs01ext\" xmlns:amgcommondictionary=\"http://www.amg.net.pl/amg/alfresco/model/amgcommondictionary\" xmlns:slideshare=\"http://www.alfresco.org/model/publishing/slideshare/1.0\" xmlns:rn=\"http://www.alfresco.org/model/rendition/1.0\" xmlns:tpformfolder=\"http://www.amg.net.pl/alfresco/model/tpformfolder\" xmlns:tpfaq=\"http://www.amg.net.pl/salsa/alfresco/model/tpfaq\" xmlns:pubwf=\"http://www.alfresco.org/model/publishingworkflow/1.0\" xmlns:cmiscustom=\"http://www.alfresco.org/model/cmis/custom\" xmlns:categorizedlistdictionary=\"http://www.amg.net.pl/amg/alfresco/model/categorizedlistdictionary\" xmlns:rc=\"http://www.alfresco.org/model/remotecredentials/1.0\" xmlns:jcr=\"http://www.jcp.org/jcr/1.0\" xmlns:tppublication=\"http://www.amg.net.pl/isana/alfresco/model/tppublication\" xmlns:ia=\"http://www.alfresco.org/model/calendar\" xmlns:tpfastlink=\"http://www.amg.net.pl/isana/alfresco/model/tpfastlink\" xmlns:rule=\"http://www.alfresco.org/model/rule/1.0\" xmlns:tpemailtemplate=\"http://www.amg.net.pl/salsa/alfresco/model/tpemailtemplate\" xmlns:tptinymcelink=\"http://www.amg.net.pl/isana/alfresco/model/tptinymcelink\" xmlns:categorizeddictionary=\"http://www.amg.net.pl/amg/alfresco/model/categorizeddictionary\" xmlns:commonsimple=\"http://www.amg.net.pl/common/alfresco/model/commonsimple\" xmlns:tpmodemfolder=\"http://www.amg.net.pl/salsa/alfresco/model/tpmodemfolder\" xmlns:tpmailingcampaigntemplate=\"http://www.amg.net.pl/salsa/alfresco/model/tpmailingcampaigntemplate\" xmlns:tpsymlink=\"http://www.amg.net.pl/salsa/alfresco/model/tpsymlink\" xmlns:audio=\"http://www.alfresco.org/model/audio/1.0\" xmlns:tprpkmock=\"http://www.amg.net.pl/salsa/alfresco/model/tprpkmock\" xmlns:alf=\"http://www.alfresco.org\" xmlns:tpaccessoryfolder=\"http://www.amg.net.pl/alfresco/model/tpaccessoryfolder\" xmlns:cmis=\"http://www.alfresco.org/model/cmis/1.0/cs01\" xmlns:tprule=\"http://www.amg.net.pl/alfresco/model/tprule\" xmlns:onpskinfile=\"http://www.amg.net.pl/isana/alfresco/model/onpskinfile\" xmlns:amgconfiguration=\"http://www.amg.net.pl/amg/alfresco/model/amgconfiguration\" xmlns:bpm=\"http://www.alfresco.org/model/bpm/1.0\" xmlns:tptinymcepicture=\"http://www.amg.net.pl/isana/alfresco/model/tptinymcepicture\" xmlns:tpbinary=\"http://www.amg.net.pl/isana/alfresco/model/tpbinary\" xmlns:tpmailingcampaigninstance=\"http://www.amg.net.pl/salsa/alfresco/model/tpmailingcampaigninstance\" xmlns:tptutorial=\"http://www.amg.net.pl/alfresco/model/tptutorial\" xmlns:tpneoparam=\"http://www.amg.net.pl/alfresco/model/tpneoparam\" xmlns:act=\"http://www.alfresco.org/model/action/1.0\" xmlns:formactionsdictionary=\"http://www.amg.net.pl/amg/alfresco/model/formactionsdictionary\">\n      <view:aspects>\n          <tppublication:publishable></tppublication:publishable>\n       <cm:auditable></cm:auditable>\n         <sys:referenceable></sys:referenceable>\n       <sys:localized></sys:localized>\n       <app:inlineeditable></app:inlineeditable>\n         <cm:versionable></cm:versionable>\n       </view:aspects>\n       <view:acl></view:acl>\n     <view:properties>\n       <cm:name>%s</cm:name>\n         <tpsimpleoffer:main_service>%s</tpsimpleoffer:main_service>\n       <tpsimpleoffer:speed>%s</tpsimpleoffer:speed>\n         <tpsimpleoffer:priority>%s</tpsimpleoffer:priority>\n       <tpsimpleoffer:priority_for_custom_offer>%s</tpsimpleoffer:priority_for_custom_offer>\n         <tpsimpleoffer:promotion>%s</tpsimpleoffer:promotion>\n         <tpsimpleoffer:gadget>%s</tpsimpleoffer:gadget>\n       <tpsimpleoffer:available_gadgets>%s</tpsimpleoffer:available_gadgets>\n         <tpsimpleoffer:promo_group_names>%s</tpsimpleoffer:promo_group_names>\n         <tpsimpleoffer:additional_selected_products>%s</tpsimpleoffer:additional_selected_products>\n       <tpsimpleoffer:modems>%s</tpsimpleoffer:modems>\n         <tpsimpleoffer:ribbon>%s</tpsimpleoffer:ribbon>\n       <tpsimpleoffer:tv_package>%s</tpsimpleoffer:tv_package>\n       <tpsimpleoffer:tv_package_description>%s</tpsimpleoffer:tv_package_description>\n       <tpsimpleoffer:additional_tv_package>%s</tpsimpleoffer:additional_tv_package>\n         <tpsimpleoffer:ncplus_packages>%s</tpsimpleoffer:ncplus_packages>\n         <tpsimpleoffer:channels_number>%s</tpsimpleoffer:channels_number>\n          <tpsimpleoffer:market_regulation>%s</tpsimpleoffer:market_regulation>\n         <tpsimpleoffer:default_offer>%s</tpsimpleoffer:default_offer>\n         <cm:creator>swobodam</cm:creator>\n         <cm:created>2017-02-07T15:48:27.684+01:00</cm:created>\n        <cm:modifier>swobodam</cm:modifier>\n       <cm:modified>2017-02-09T06:20:52.777+01:00</cm:modified>\n          <sys:store-identifier>SpacesStore</sys:store-identifier>\n          <cm:initialVersion>true</cm:initialVersion>\n       <cm:versionLabel>1.0</cm:versionLabel>\n        <cm:autoVersionOnUpdateProps>true</cm:autoVersionOnUpdateProps>\n       <sys:store-protocol>workspace</sys:store-protocol>\n        <sys:locale>pl_</sys:locale>\n          <cm:autoVersion>true</cm:autoVersion>\n       </view:properties>\n      </tpsimpleoffer:tp_simple_offer>";
    private final String xmlTemplateFooter = "</view:view>";
    private final SimpleDateFormat sdf = new SimpleDateFormat("_yyyy_MM_dd_HHmm");

    /**
     * Czyści folder tymczasowy i zleca zapis konkretnych ofert do poszczególnych plików ZIP. Każdy ZIP to inny
     * formularz (neo-ins, 3p-mod, itp...)
     * 
     * @param parsedOffersPack
     *            paczka ofert przeparsowanych z CSV.
     * @param outputFolder
     *            folder docelowy (jeśli pusty to %TEMP%/oferty)
     */
    public void saveOffersToZIPFiles(ParsedOffersPack parsedOffersPack, String outputFolder) {
        if (!parsedOffersPack.getAllOffers().isEmpty()) {
            if (outputFolder == null) {
                outputFolder = System.getProperty("java.io.tmpdir") + "/oferty/";
                if (!new File(outputFolder).exists()) {
                    new File(outputFolder).mkdirs();
                } else {
                    // usuwam wszystkie istniejące oferty w katalogu tymczasowym
                    File[] files = new File(outputFolder).listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (!f.isDirectory() && f.getName().endsWith("zip")) {
                                f.delete();
                            }
                        }
                    }
                }
            }
            String currentDate = sdf.format(new Date());
            prepareAndSave(parsedOffersPack.getNeoInsOffers(), FormId.NEO_INS.getValue() + currentDate, outputFolder);
            prepareAndSave(parsedOffersPack.getNeoModOffers(), FormId.NEO_MOD.getValue() + currentDate, outputFolder);
//            prepareAndSave(parsedOffersPack.getNeoInsBizOffers(), FormId.NEO_INS_BIZ.getValue() + currentDate,
//                    outputFolder);
//            prepareAndSave(parsedOffersPack.getNeoModBizOffers(), FormId.NEO_MOD_BIZ.getValue() + currentDate,
//                    outputFolder);
            prepareAndSave(parsedOffersPack.getTrplInsOffers(), FormId.TRPL_INS.getValue() + currentDate, outputFolder);
            prepareAndSave(parsedOffersPack.getTrplModOffers(), FormId.TRPL_MOD.getValue() + currentDate, outputFolder);
        }
    }

    /**
     * Zapisuje oferty do pliku XML i ZIP.
     * 
     * @param offers
     *            oferty do umieszczenia w pliku.
     * @param formPrefix
     *            przedrostek nazwy
     * @param outputFolderPath
     */
    private void prepareAndSave(List<SimpleOffer> offers, String formPrefix, String outputFolderPath) {
        if (offers.size() > 0) {
            String xmlFilename = formPrefix + xmlFilenameSuffix;
            // saveOffersToXMLFile(neoInsOffers, OUTPUT_PATH + xmlFilename);
            saveOffersToZIPFile(offers, outputFolderPath + formPrefix + zipFilenameSuffix, xmlFilename);
        }
    }

    /**
     * Zapisuje oferty do konkretnego pliku ZIP.
     * 
     * @param resultList
     *            lista ofert do zapisania
     * @param filename
     *            nazwa pliku ZIP
     * @param xmlFilename
     *            nazwa pliku XML wewnątrz ZIP (bez znaczenia)
     */
    private void saveOffersToZIPFile(List<SimpleOffer> resultList, String filename, String xmlFilename) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);
            ZipOutputStream zos = new ZipOutputStream(fos);
            ZipEntry ze = new ZipEntry(xmlFilename);
            zos.putNextEntry(ze);
            zos.write(xmlTemplateHeader.getBytes());
            for (SimpleOffer s : resultList) {
                zos.write(String.format(xmlTemplateMain, s.getName(), s.getMainService().toString(), s.getSpeed(),
                        s.getPriority(), s.getPriorityForCustomOffer(), s.getPromotion(), s.getGadget(),
                        s.getAvailableGadgets(), s.getPromoGroupNames(), s.getAdditionalProducts(), s.getModems(),
                        s.getRibbon(), s.getTvPackage(), s.getTvPackageDescription(), s.getNtoPackage(),
                        s.getNcplusPackages(), s.getChannelsNumber(), s.getMarket(), s.getDefaultOffer()).getBytes());
                zos.write("\r\n".getBytes());
            }
            zos.write(xmlTemplateFooter.getBytes());
            zos.closeEntry();
            zos.close();
            fos.close();
            logDebug("ZIP " + filename + " saved");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Zapisuje oferty do pliku XML
     * 
     * @param resultList
     *            lista ofert
     * @param filename
     *            nazwa pliku
     */
    private void saveOffersToXMLFile(List<SimpleOffer> resultList, String filename) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(new File(filename));
            Writer out = new OutputStreamWriter(fos);
            out.write(xmlTemplateHeader);
            for (SimpleOffer s : resultList) {
                out.write(String.format(xmlTemplateMain, s.getName(), s.getMainService(), s.getSpeed(),
                        s.getPriority(), s.getPriorityForCustomOffer(), s.getPromotion(), s.getGadget(),
                        s.getAvailableGadgets(), s.getPromoGroupNames(), s.getAdditionalProducts(), s.getModems(),
                        s.getRibbon(), s.getTvPackage(), s.getTvPackageDescription(), s.getNtoPackage(),
                        s.getNcplusPackages(), s.getChannelsNumber(), s.getMarket(), s.getDefaultOffer()));
                out.write("\r\n");
            }
            out.write(xmlTemplateFooter);
            out.close();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logDebug("XML " + filename + " saved");
    }

    private static void logDebug(String msg) {
        System.out.println(msg);
    }
}
