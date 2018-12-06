package be.gobius.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Leden implements Serializable {
    private static final Long serialVersionUID = 325564572L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name="info", table="leden", columnDefinition="character varying (50)", length=50, nullable=false)
    private String info;
    @Column(name="naam", table="leden", columnDefinition="character varying (25)", length=25, nullable=false)
    private String naam;
    @Column(name="voornaam", table="leden", columnDefinition="character varying (25)", length=25, nullable=false)
    private String voornaam;
    @Column(name="telefoon", table="leden", columnDefinition="character varying (20)", length=20, nullable=false)
    private String telefoon;
    @Column(name="gsm", table="leden", columnDefinition="character varying (20)", length=20, nullable=false)
    private String gsm;
    @Column(name="email", table="leden", columnDefinition="character varying (255)", length=255, nullable=false)
    private String email;
    @Column(name="brevet", table="leden", columnDefinition="character varying (5)", length=5, nullable=false)
    private String brevet;
    @Column(name="brevetRaw", table="leden", columnDefinition="NUMBER (2, 0)", length=2, scale=0, nullable=false)
    private int brevetRaw;
    @Column(name="anderBrevet", table="leden", columnDefinition="character varying (80)", length=80, nullable=false)
    private String anderBrevet;
    @Column(name="verzekerd", table="leden", columnDefinition="character varying (3)", length=3, nullable=false)
    private String verzekerd; // JA, NEE
    @Column(name="timestamp", table="leden", columnDefinition="character varying (10)", length=10, nullable=false)
    private String timestamp; // dd/mm/ccyy
    @Column(name="gebdatum", table="leden", columnDefinition="character varying (10)", length=10, nullable=false)
    private String gebdatum; // ccyy-mm-dd
    @Column(name="adres", table="leden", columnDefinition="character varying (255)", length=255, nullable=false)
    private String adres;
    @Column(name="actief", table="leden", columnDefinition="NUMBER (1, 0)", length=1, scale=0, nullable=false)
    private int actief; // 0= non-actief, 1=actief, 2=dit jaar gestopt

    public Leden() { }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getInfo() {return info;}
    public void setInfo(String info) {this.info = info;}

    public String getNaam() {return naam;}
    public void setNaam(String naam) {this.naam = naam;}

    public String getVoornaam() {return voornaam;}
    public void setVoornaam(String voornaam) {this.voornaam = voornaam;}

    public String getTelefoon() {return telefoon;}
    public void setTelefoon(String telefoon) {this.telefoon = telefoon;}

    public String getGsm() {return gsm;}
    public void setGsm(String gsm) {this.gsm = gsm;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getBrevet() {return brevet;}
    public void setBrevet(String brevet) {this.brevet = brevet;}

    public int getBrevetRaw() {return brevetRaw;}
    public void setBrevetRaw(int brevetRaw) {this.brevetRaw = brevetRaw;}

    public String getAnderBrevet() {return anderBrevet;}
    public void setAnderBrevet(String anderBrevet) {this.anderBrevet = anderBrevet;}

    public String getVerzekerd() {return verzekerd;}
    public void setVerzekerd(String verzekerd) {this.verzekerd = verzekerd;}

    public String getTimestamp() {return timestamp;}
    public void setTimestamp(String timestamp) {this.timestamp = timestamp;}

    public String getAdres() {return adres;}
    public void setAdres(String adres) {this.adres = adres;}

    public String getGebdatum() {return gebdatum;}
    public void setGebdatum(String gebdatum) {this.gebdatum = gebdatum;}

    public int getActief() {return actief;}
    public void setActief(int actief) {this.actief = actief;}

    @Override
    public String toString() {
        return "Leden{" +
                "actief=" + actief +
                ", info='" + info + '\'' +
                ", naam='" + naam + '\'' +
                ", voornaam='" + voornaam + '\'' +
                ", telefoon='" + telefoon + '\'' +
                ", gsm='" + gsm + '\'' +
                ", email='" + email + '\'' +
                ", brevet='" + brevet + '\'' +
                ", brevetRaw=" + brevetRaw +
                ", anderBrevet='" + anderBrevet + '\'' +
                ", verzekerd='" + verzekerd + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", gebdatum='" + gebdatum + '\'' +
                ", adres='" + adres + '\'' +
                '}';
    }
}