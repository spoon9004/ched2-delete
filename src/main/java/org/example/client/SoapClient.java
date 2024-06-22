package org.example.client;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;

@Component
public class SoapClient {

    private static final Logger logger = LogManager.getLogger(SoapClient.class);

    public void sendSoapRequest(String url, String username, String password, List<String> uuids ) throws IOException {
        for (String uuid : uuids) {
            String soapRequest = buildSoapRequest(uuid);
            logger.info("Сформирован запрос по ID документа:" + uuid);
            String soapResponse = sendSoapMessage(url, username, password, soapRequest);
            logger.info("SOAP Response:" + soapResponse);
        }
    }

    private String buildSoapRequest(String uuid) {
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:cus=\"http://cloud.mos.ru/customWebService2/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <cus:DeleteDocuments>\n" +
                "         <Documents>" + uuid + "</Documents>\n" +
                "      </cus:DeleteDocuments>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";

    }

    private String sendSoapMessage(String urlStr, String username, String password, String soapRequest) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeaderValue = "Basic " + encodedAuth;

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty("Authorization", authHeaderValue);
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(soapRequest.getBytes());
        }

        StringBuilder response = new StringBuilder();
        try (InputStream inputStream = connection.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                response.append(new String(buffer, 0, bytesRead));
            }
        }

        return response.toString();
    }
}
