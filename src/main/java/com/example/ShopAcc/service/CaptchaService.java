package com.example.ShopAcc.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Service
public class CaptchaService {

    // Character set for CAPTCHA, avoiding confusing characters
    private static final String CHARACTERS = "abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final Logger logger = Logger.getLogger(CaptchaService.class.getName());
    private static final int DEFAULT_CAPTCHA_LENGTH = 5;

    // ConcurrentHashMap to store CAPTCHA values associated with session IDs
    private final Map<String, String> captchaStore = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    /**
     * Generates a random CAPTCHA string.
     *
     * @return the generated CAPTCHA string
     */
    public String createCaptcha() {
        StringBuilder captcha = new StringBuilder();

        // Build a random CAPTCHA string of default length
        for (int i = 0; i < DEFAULT_CAPTCHA_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            captcha.append(CHARACTERS.charAt(index));
        }

        String generatedCaptcha = captcha.toString();
        logger.info("Generated CAPTCHA: " + generatedCaptcha);
        return generatedCaptcha;
    }

    /**
     * Generates a CAPTCHA for a specific session ID and stores it in the map.
     *
     * @param id the session ID
     * @return the generated CAPTCHA
     */
    public String generateCaptchaForId(String id) {
        String captcha = createCaptcha();
        captchaStore.put(id, captcha); // Store the CAPTCHA with the session ID
        logger.info("Assigned CAPTCHA to ID: " + id + " -> " + captcha);
        return captcha;
    }

    /**
     * Verifies if the input CAPTCHA matches the stored CAPTCHA for the given session ID.
     *
     * @param id the session ID
     * @param inputCaptcha the CAPTCHA input by the user
     * @return true if the CAPTCHA is correct, false otherwise
     */
    public boolean verifyCaptcha(String id, String inputCaptcha) {
        String sessionCaptcha = captchaStore.get(id); // Retrieve stored CAPTCHA
        if (inputCaptcha == null || sessionCaptcha == null) {
            return false;
        }
        // Check if the input CAPTCHA matches the stored one, case-insensitively
        boolean isMatch = inputCaptcha.equalsIgnoreCase(sessionCaptcha);
        logger.info("CAPTCHA verification for ID " + id + ": " + (isMatch ? "matched" : "did not match"));
        return isMatch;
    }

    /**
     * Removes the stored CAPTCHA for the given session ID.
     *
     * @param id the session ID
     */
    public void removeCaptcha(String id) {
        captchaStore.remove(id); // Remove the CAPTCHA from the map
        logger.info("Removed CAPTCHA for ID: " + id);
    }
}
