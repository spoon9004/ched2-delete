package org.example.controller;

import org.example.client.SoapClient;
import org.example.service.UuidReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SoapClientController {

    private final SoapClient soapClient;

    private final UuidReaderService uuidReaderService;

    public SoapClientController(SoapClient soapClient, UuidReaderService uuidReaderService) {
        this.soapClient = soapClient;
        this.uuidReaderService = uuidReaderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @PostMapping("/sendRequest")
    public String sendRequest(
            @RequestParam String url,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String uuidFilePath,
            Model model) {
        try {
            List<String> uuids = uuidReaderService.readUuids(uuidFilePath);
            soapClient.sendSoapRequest(url, username, password, uuids);
        } catch (Exception e) {
            model.addAttribute("response", "Ошибка: " + e.getMessage());
        }
        return "index";
    }
}
