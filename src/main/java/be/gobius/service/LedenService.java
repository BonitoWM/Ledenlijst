package be.gobius.service;

import be.gobius.domain.Leden;
import be.gobius.repository.LedenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LedenService {

    public static final Logger LOGGER = LoggerFactory.getLogger((LedenService.class));

    @Autowired
    private LedenRepository repo;

    public Iterable<Leden> getAll() {
        return this.repo.findAll();
    }

    public int updateAllStoppedToInactive() {
        return repo.updateAllStoppedToInactive();
    }

    public int updateAllActiveToStopped() {
        return repo.updateAllActiveToStopped();
    }

    public Leden findByNaamAndVoornaam(String naam, String voornaam) {
        Optional<Leden> lidByNaamAndVoornaam = this.repo.findByNaamAndVoornaam(naam, voornaam);
        return lidByNaamAndVoornaam.orElse(null);
    }

    public Leden createUpdate(Leden lid) {
        return this.repo.save(lid);
    }
}