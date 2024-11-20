package catolica.edu.pot0tickets.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class FirebaseStorageService {

    @PostConstruct
    public void initialize() throws IOException {
        // Inicializar Firebase desde un archivo de configuración JSON
        ClassLoader classLoader = getClass().getClassLoader(); // Usa el ClassLoader de la clase actual
        File file = new File(Objects.requireNonNull(classLoader.getResource("firebase-config.json")).getFile());

        try (FileInputStream serviceAccount = new FileInputStream(file)) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("pot1-tickets.appspot.com")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // Asegúrate de inicializar solo una vez
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            System.out.println("Error inicializando Firebase: " + e.getMessage());
            throw e;
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        var blob = StorageClient.getInstance()
                .bucket()
                .create(file.getOriginalFilename(), file.getInputStream(), file.getContentType());
        return blob.getMediaLink();
    }
}


