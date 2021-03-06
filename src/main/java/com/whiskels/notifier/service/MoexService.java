package com.whiskels.notifier.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
@Slf4j
public class MoexService {
    @Value("${moex.url}")
    private String moexUrl;

    @Value("${moex.usd}")
    private String moexUsd;

    @Value("${moex.eur}")
    private String moexEur;

    @Getter
    private double usdRate;
    @Getter
    private double eurRate;

    @PostConstruct
    private void initExchangeRates() {
        update();
    }

    public void update() {
        updateExchangeRates();
    }

    /**
     * Updates USD/RUB and EUR/RUB exchange rates using MOEX data
     */
    private void updateExchangeRates() {
        log.info("updating exchange rates");
        // Getting moex exchange rates string
        try {
            String moexContent = IOUtils.toString(new URL(moexUrl), StandardCharsets.UTF_8);
            // Removing HTML tags
            moexContent = moexContent.replaceAll("\\<.*?\\>", "");

            // Converting data to string array
            String[] moexArray = moexContent.split("\n");

            // Mapping data
            final int moexDataLength = (moexArray.length - 1) / 2;
            HashMap<String, String> ratesMap = new HashMap<>(moexDataLength);
            for (int i = 0; i < moexDataLength; i++) {
                ratesMap.put(moexArray[i + 1], moexArray[i + 1 + moexDataLength]);
            }

            if (!ratesMap.isEmpty()) {
                usdRate = Double.parseDouble(ratesMap.get(moexUsd));
                eurRate = Double.parseDouble(ratesMap.get(moexEur));
            }
        } catch (IOException e) {
            log.error("Exception while trying to get MOEX data: {}", e.toString());
        } catch (NumberFormatException e) {
            log.error("Exception while trying to update exchange rate: {}", e.toString());
        }
    }
}
