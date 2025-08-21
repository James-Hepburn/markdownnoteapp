package com.example.markdownnoteapp.service;

import com.example.markdownnoteapp.model.Note;
import com.example.markdownnoteapp.repository.NoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class FileService {
    private NoteRepository repo;

    public void saveFile (MultipartFile file) {
        try {
            String content = new String (file.getBytes ());
            Note note = new Note (file.getOriginalFilename (), content);
            this.repo.save (note);
        } catch (IOException e) {
            throw new RuntimeException ("Failed to read file content", e);
        }
    }

    public String readFileContent (MultipartFile file) {
        try {
            return new String (file.getBytes ());
        } catch (IOException e) {
            throw new RuntimeException ("Failed to read file content", e);
        }
    }

    public List <String> listFiles () {
        return this.repo.findAll ()
                .stream ()
                .map (Note::getTitle)
                .collect (Collectors.toList ());
    }

    public String getFile (String filename) {
        return this.repo.findAll ()
                .stream ()
                .filter (note -> note.getTitle ().equals (filename))
                .map (Note::getMarkdownContent)
                .findFirst ()
                .orElse (null);
    }
}
