package be.gobius.repository;

import be.gobius.domain.Leden;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface LedenRepository extends JpaRepository<Leden, Long> {
    // hier worden de methodes geschreven die niet standaard in JPA of CRUD voorkomen //
    Optional<Leden> findByNaamAndVoornaam(String naam, String voornaam);

    // zet alle gestopte leden op non-actief
    @Modifying
    @Query(value = "UPDATE leden.leden SET actief = 0 WHERE actief = 2", nativeQuery = true)
    @Transactional
    int updateAllStoppedToInactive();

    // zet alle actieve leden op gestopt (door xlsx te verwerken worden ze weer op actief gezet)
    @Modifying
    @Query(value = "UPDATE leden.leden SET actief = 2 WHERE actief = 1", nativeQuery = true)
    @Transactional
    int updateAllActiveToStopped();
}
