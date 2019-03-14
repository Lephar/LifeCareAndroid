package fit.lifecare.lifecare.DatabaseClasses;


import java.io.Serializable;
import java.util.ArrayList;

public class ProgramlarimData implements Serializable {

    private ArrayList<String> aksamYemegi;
    private String aksamYemegiCalory;
    private ArrayList<String> alternatif;
    private String alternatifCalory;
    private ArrayList<String> birinciAraOgun;
    private String birinciAraOgunCalory;
    private String date;
    private ArrayList<String> ikinciAraOgun;
    private String ikinciAraOgunCalory;
    private String name;
    private ArrayList<String> ogleYemegi;
    private String ogleYemegiCalory;
    private String program_name;
    private ArrayList<String> sabahKahvaltisi;
    private String sabahKahvaltisiCalory;
    private String toplamCalory;
    private ArrayList<String> ucuncuAraOgun;
    private String ucuncuAraOgunCalory;


    public ProgramlarimData() {

    }
    
    public ProgramlarimData(ArrayList<String> aksamYemegi, String aksamYemegiCalory, ArrayList<String> alternatif, String alternatifCalory, ArrayList<String> birinciAraOgun, String birinciAraOgunCalory, String date, ArrayList<String> ikinciAraOgun, String ikinciAraOgunCalory, String name, ArrayList<String> ogleYemegi, String ogleYemegiCalory, String program_name, ArrayList<String> sabahKahvaltisi, String sabahKahvaltisiCalory, String toplamCalory, ArrayList<String> ucuncuAraOgun, String ucuncuAraOgunCalory) {
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
        this.toplamCalory = toplamCalory;
        this.ucuncuAraOgun = ucuncuAraOgun;
        this.ucuncuAraOgunCalory = ucuncuAraOgunCalory;
    }
    
    public ArrayList<String> getAksamYemegi() {
        return aksamYemegi;
    }
    
    public void setAksamYemegi(ArrayList<String> aksamYemegi) {
        this.aksamYemegi = aksamYemegi;
    }
    
    public String getAksamYemegiCalory() {
        return aksamYemegiCalory;
    }
    
    public void setAksamYemegiCalory(String aksamYemegiCalory) {
        this.aksamYemegiCalory = aksamYemegiCalory;
    }
    
    public ArrayList<String> getAlternatif() {
        return alternatif;
    }
    
    public void setAlternatif(ArrayList<String> alternatif) {
        this.alternatif = alternatif;
    }
    
    public String getAlternatifCalory() {
        return alternatifCalory;
    }
    
    public void setAlternatifCalory(String alternatifCalory) {
        this.alternatifCalory = alternatifCalory;
    }
    
    public ArrayList<String> getBirinciAraOgun() {
        return birinciAraOgun;
    }
    
    public void setBirinciAraOgun(ArrayList<String> birinciAraOgun) {
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
    
    public ArrayList<String> getIkinciAraOgun() {
        return ikinciAraOgun;
    }
    
    public void setIkinciAraOgun(ArrayList<String> ikinciAraOgun) {
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
    
    public ArrayList<String> getOgleYemegi() {
        return ogleYemegi;
    }
    
    public void setOgleYemegi(ArrayList<String> ogleYemegi) {
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
    
    public ArrayList<String> getSabahKahvaltisi() {
        return sabahKahvaltisi;
    }
    
    public void setSabahKahvaltisi(ArrayList<String> sabahKahvaltisi) {
        this.sabahKahvaltisi = sabahKahvaltisi;
    }
    
    public String getSabahKahvaltisiCalory() {
        return sabahKahvaltisiCalory;
    }
    
    public void setSabahKahvaltisiCalory(String sabahKahvaltisiCalory) {
        this.sabahKahvaltisiCalory = sabahKahvaltisiCalory;
    }
    
    public String getToplamCalory() {
        return toplamCalory;
    }
    
    public void setToplamCalory(String toplamCalory) {
        this.toplamCalory = toplamCalory;
    }
    
    public ArrayList<String> getUcuncuAraOgun() {
        return ucuncuAraOgun;
    }
    
    public void setUcuncuAraOgun(ArrayList<String> ucuncuAraOgun) {
        this.ucuncuAraOgun = ucuncuAraOgun;
    }
    
    public String getUcuncuAraOgunCalory() {
        return ucuncuAraOgunCalory;
    }
    
    public void setUcuncuAraOgunCalory(String ucuncuAraOgunCalory) {
        this.ucuncuAraOgunCalory = ucuncuAraOgunCalory;
    }
}