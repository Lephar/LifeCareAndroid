package fit.lifecare.lifecare.DatabaseClasses;


import java.io.Serializable;

public class ProgramlarimData implements Serializable {

    private String aksamYemegi;
    private String aksamYemegiCalory;
    private String alternatif;
    private String alternatifCalory;
    private String birinciAraOgun;
    private String birinciAraOgunCalory;
    private String date;
    private String ikinciAraOgun;
    private String ikinciAraOgunCalory;
    private String name;
    private String ogleYemegi;
    private String ogleYemegiCalory;
    private String program_name;
    private String sabahKahvaltisi;
    private String sabahKahvaltisiCalory;
    private String ucuncuAraOgun;
    private String ucuncuAraOgunCalory;


    public ProgramlarimData() {

    }

    public ProgramlarimData(String aksamYemegi, String aksamYemegiCalory, String alternatif, String alternatifCalory, String birinciAraOgun, String birinciAraOgunCalory, String date, String ikinciAraOgun, String ikinciAraOgunCalory, String name, String ogleYemegi, String ogleYemegiCalory, String program_name, String sabahKahvaltisi, String sabahKahvaltisiCalory, String ucuncuAraOgun, String ucuncuAraOgunCalory) {
        this.aksamYemegi = aksamYemegi;
        this.aksamYemegiCalory = aksamYemegiCalory;
        this.alternatif = alternatif;
        this.alternatifCalory = alternatifCalory;
        this.birinciAraOgun = birinciAraOgun;
        this.birinciAraOgunCalory = birinciAraOgunCalory;
        this.date = date;
        this.ikinciAraOgun = ikinciAraOgun;
        this.ikinciAraOgunCalory = ikinciAraOgunCalory;
        this.name = name;
        this.ogleYemegi = ogleYemegi;
        this.ogleYemegiCalory = ogleYemegiCalory;
        this.program_name = program_name;
        this.sabahKahvaltisi = sabahKahvaltisi;
        this.sabahKahvaltisiCalory = sabahKahvaltisiCalory;
        this.ucuncuAraOgun = ucuncuAraOgun;
        this.ucuncuAraOgunCalory = ucuncuAraOgunCalory;
    }

    public String getAksamYemegi() {
        return aksamYemegi;
    }

    public void setAksamYemegi(String aksamYemegi) {
        this.aksamYemegi = aksamYemegi;
    }

    public String getAksamYemegiCalory() {
        return aksamYemegiCalory;
    }

    public void setAksamYemegiCalory(String aksamYemegiCalory) {
        this.aksamYemegiCalory = aksamYemegiCalory;
    }

    public String getAlternatif() {
        return alternatif;
    }

    public void setAlternatif(String alternatif) {
        this.alternatif = alternatif;
    }

    public String getAlternatifCalory() {
        return alternatifCalory;
    }

    public void setAlternatifCalory(String alternatifCalory) {
        this.alternatifCalory = alternatifCalory;
    }

    public String getBirinciAraOgun() {
        return birinciAraOgun;
    }

    public void setBirinciAraOgun(String birinciAraOgun) {
        this.birinciAraOgun = birinciAraOgun;
    }

    public String getBirinciAraOgunCalory() {
        return birinciAraOgunCalory;
    }

    public void setBirinciAraOgunCalory(String birinciAraOgunCalory) {
        this.birinciAraOgunCalory = birinciAraOgunCalory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIkinciAraOgun() {
        return ikinciAraOgun;
    }

    public void setIkinciAraOgun(String ikinciAraOgun) {
        this.ikinciAraOgun = ikinciAraOgun;
    }

    public String getIkinciAraOgunCalory() {
        return ikinciAraOgunCalory;
    }

    public void setIkinciAraOgunCalory(String ikinciAraOgunCalory) {
        this.ikinciAraOgunCalory = ikinciAraOgunCalory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOgleYemegi() {
        return ogleYemegi;
    }

    public void setOgleYemegi(String ogleYemegi) {
        this.ogleYemegi = ogleYemegi;
    }

    public String getOgleYemegiCalory() {
        return ogleYemegiCalory;
    }

    public void setOgleYemegiCalory(String ogleYemegiCalory) {
        this.ogleYemegiCalory = ogleYemegiCalory;
    }

    public String getProgram_name() {
        return program_name;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public String getSabahKahvaltisi() {
        return sabahKahvaltisi;
    }

    public void setSabahKahvaltisi(String sabahKahvaltisi) {
        this.sabahKahvaltisi = sabahKahvaltisi;
    }

    public String getSabahKahvaltisiCalory() {
        return sabahKahvaltisiCalory;
    }

    public void setSabahKahvaltisiCalory(String sabahKahvaltisiCalory) {
        this.sabahKahvaltisiCalory = sabahKahvaltisiCalory;
    }

    public String getUcuncuAraOgun() {
        return ucuncuAraOgun;
    }

    public void setUcuncuAraOgun(String ucuncuAraOgun) {
        this.ucuncuAraOgun = ucuncuAraOgun;
    }

    public String getUcuncuAraOgunCalory() {
        return ucuncuAraOgunCalory;
    }

    public void setUcuncuAraOgunCalory(String ucuncuAraOgunCalory) {
        this.ucuncuAraOgunCalory = ucuncuAraOgunCalory;
    }
}