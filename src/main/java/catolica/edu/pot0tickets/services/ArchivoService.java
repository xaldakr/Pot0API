package catolica.edu.pot0tickets.services;


import catolica.edu.pot0tickets.models.Archivo;
import catolica.edu.pot0tickets.repositories.ArchivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchivoService {

    @Autowired
    private ArchivoRepository archivoRepository;

    public Archivo saveArchivo(Archivo archivo) {
        return archivoRepository.save(archivo);
    }
}

