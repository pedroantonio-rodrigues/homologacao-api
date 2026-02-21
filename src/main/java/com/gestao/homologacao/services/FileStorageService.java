package com.gestao.homologacao.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    //Define onde os arquivos serão salvos no computador
    private final Path root = Paths.get("uploads");

    public String save(MultipartFile file){
        try {
            // Cria a pasta "uploads" se não existir
            if (!Files.exists(root)) Files.createDirectory(root);

            String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(fileName));
            return fileName; // Retorna o nome do arquivo para ser salvo no banco
        } catch (IOException e){
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

}
