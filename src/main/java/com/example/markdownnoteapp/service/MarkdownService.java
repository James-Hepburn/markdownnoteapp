package com.example.markdownnoteapp.service;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

@Service
public class MarkdownService {
    private Parser parser;
    private HtmlRenderer renderer;

    public MarkdownService () {
        this.parser = Parser.builder ().build ();
        this.renderer = HtmlRenderer.builder ().build ();
    }

    public String convertToHtml (String markdown) {
        if (markdown == null || markdown.isEmpty ()) {
            return "";
        }

        return this.renderer.render (this.parser.parse (markdown));
    }
}
