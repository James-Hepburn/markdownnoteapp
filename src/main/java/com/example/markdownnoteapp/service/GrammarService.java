package com.example.markdownnoteapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class GrammarService {
    private RestTemplate template;
    private ObjectMapper mapper = new ObjectMapper ();
    private String api = "https://api.languagetool.org/v2/check";

    public GrammarService (RestTemplate template) {
        this.template = template;
    }

    public String checkGrammar (MultipartFile file) {
        try {
            String content = new String (file.getBytes ());
            return checkGrammar (content);
        } catch (IOException e) {
            throw new RuntimeException ("Failed to read file for grammar check", e);
        }
    }

    public String checkGrammar (String markdownContent) {
        if (markdownContent == null || markdownContent.isEmpty ()) {
            return "No content to check";
        }

        HttpHeaders headers = new HttpHeaders ();
        headers.setContentType (MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap <String, String> parameters = new LinkedMultiValueMap <>();
        parameters.add ("text", markdownContent);
        parameters.add ("language", "en-US");

        HttpEntity <MultiValueMap <String, String>> request = new HttpEntity <>(parameters, headers);
        ResponseEntity <String> response = this.template.postForEntity (this.api, request, String.class);

        return formatGrammarResponse (response.getBody ());
    }

    public String formatGrammarResponse (String jsonResponse) {
        StringBuilder result = new StringBuilder ();

        try {
            JsonNode root = this.mapper.readTree (jsonResponse);
            JsonNode matches = root.path ("matches");

            if (matches.isEmpty ()) {
                return "No grammar issues found";
            }

            for (JsonNode match : matches) {
                String message = match.path ("message").asText ();
                String context = match.path ("context").path ("text").asText ();
                int offset = match.path ("context").path ("offset").asInt ();
                int length = match.path ("context").path ("length").asInt ();
                String errorPart = context.substring (offset, Math.min (offset + length, context.length ()));

                result.append ("Issue: ").append (message).append ("\n");
                result.append ("   → Problem: '").append (errorPart).append ("'\n");

                JsonNode replacements = match.path ("replacements");
                if (replacements.isArray () && replacements.size () > 0) {
                    result.append ("   → Suggestions: ");
                    for (int i = 0; i < replacements.size (); i++) {
                        result.append (replacements.get (i).path("value").asText ());
                        if (i < replacements.size () - 1) result.append (", ");
                    }
                    result.append ("\n");
                }
                result.append ("\n");
            }
        } catch (Exception e) {
            return "Error parsing grammar response";
        }

        return result.toString ();
    }
}
