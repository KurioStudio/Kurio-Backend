package app.kuriobackend;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class KurioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KurioBackendApplication.class, args);
        initializeFirebase();
    }

    private static void initializeFirebase() {
        try {
            InputStream serviceAccount = KurioBackendApplication.class.getClassLoader().getResourceAsStream("firebase-service.json");

            if(serviceAccount != null) {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setStorageBucket("kurio-6ecc0.firebasestorage.app")
                        .build();

                if(FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
