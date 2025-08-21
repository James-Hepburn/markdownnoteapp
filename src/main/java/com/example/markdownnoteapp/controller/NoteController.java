package com.example.markdownnoteapp.controller;

import com.example.markdownnoteapp.service.FileService;
import com.example.markdownnoteapp.service.GrammarService;
import com.example.markdownnoteapp.service.MarkdownService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {
    private FileService fileService;
    private GrammarService grammarService;
    private MarkdownService markdownService;

    @PostMapping("/upload")
    public void uploadNote (@RequestParam("file") MultipartFile file) {
        this.fileService.saveFile (file);
    }

    @GetMapping("/{title}")
    public String getNote (@PathVariable String title) {
        return this.fileService.getFile (title);
    }

    @GetMapping
    public List <String> listNotes () {
        return this.fileService.listFiles ();
    }

    @PostMapping("/check-grammar")
    public String checkGrammar (@RequestParam("file") MultipartFile file) {
        return this.grammarService.checkGrammar (file);
    }

    @GetMapping("/{title}/render")
    public String renderNoteAsHtml (@PathVariable String title) {
        return this.markdownService.convertToHtml (this.fileService.getFile (title));
    }
}
