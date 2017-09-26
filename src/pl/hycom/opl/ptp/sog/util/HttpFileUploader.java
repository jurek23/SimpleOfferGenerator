package pl.hycom.opl.ptp.sog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Wysyła plik jako POST pod podany URL. Ustawia też parametr destination z identyfikatorem folderu gdzie mają być
 * zaimportowane oferty.
 * 
 * @author krystian.gorecki@hycom.pl
 */
public class HttpFileUploader {

    /**
     * Przygotowuje POST z wysyłką pliku.
     * 
     * @param urlString
     *            URL do serwisu importującego pliki do Alfresco
     * @param file
     *            plik zip z ofertami
     * @param paramDestination
     *            id folderu docelowego do zaimportowania ofert
     * @return server response as <code>String</code>
     */
    public String executeMultiPartRequest(String urlString, File file, String paramDestination) {
        System.out.println("importing file " + file.getAbsolutePath() + " to folder " + paramDestination + " //POST "
                + urlString);
        HttpPost postRequest = new HttpPost(urlString);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addTextBody("destination", paramDestination);
        FileBody fileBody = new FileBody(file, ContentType.create("application/zip"));
        builder.addPart("filedata", fileBody);
        postRequest.setEntity(builder.build());
        return executeRequest(postRequest);
    }

    /**
     * Właściwa wysyłka requestu i pobranie response.
     * 
     * @param request
     *            wcześniej zbudowane żądanie wypełnione poprawnymi aprametrami
     * 
     * @return response odpowiedź serwera
     */
    private static String executeRequest(HttpRequestBase request) {
        String responseString = "";
        InputStream responseStream = null;
        CloseableHttpClient client = HttpClientBuilder.create().build();
        try {
            HttpResponse response = client.execute(request);
            if (response != null) {
                HttpEntity responseEntity = response.getEntity();

                if (responseEntity != null) {
                    responseStream = responseEntity.getContent();
                    if (responseStream != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(responseStream));
                        String responseLine = br.readLine();
                        StringBuilder tempResponseString = new StringBuilder();
                        while (responseLine != null) {
                            tempResponseString.append(responseLine).append(System.getProperty("line.separator"));
                            responseLine = br.readLine();
                        }
                        br.close();
                        if (tempResponseString.length() > 0) {
                            responseString = tempResponseString.toString();
                        }
                    }
                }
            }
            client.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseString;
    }

}