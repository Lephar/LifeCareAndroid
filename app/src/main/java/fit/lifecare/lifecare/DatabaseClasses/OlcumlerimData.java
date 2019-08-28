package fit.lifecare.lifecare.DatabaseClasses;


public class OlcumlerimData {

    private String vucut_agirligi;
    private String beden_kutle_endeksi;
    private String kas_orani;
    private String yag_orani;
    private String su_orani;
    private String metabolizma_yasi;
    private String bazal_metabolizma_hizi;
    private String empedans;


    public OlcumlerimData() {
    }

    public OlcumlerimData(String vucut_agirligi, String beden_kutle_endeksi, String yag_orani, String su_orani, String kas_orani, String bazal_metabolizma_hizi, String metabolizma_yasi, String empedans) {
        this.vucut_agirligi = vucut_agirligi;
        this.beden_kutle_endeksi = beden_kutle_endeksi;
        this.yag_orani = yag_orani;
        this.su_orani = su_orani;
        this.kas_orani = kas_orani;
        this.bazal_metabolizma_hizi = bazal_metabolizma_hizi;
        this.metabolizma_yasi = metabolizma_yasi;;
        this.empedans = empedans;
    }

    public String getVucut_agirligi() {
        return vucut_agirligi;
    }

    public void setVucut_agirligi(String vucut_agirligi) {
        this.vucut_agirligi = vucut_agirligi;
    }

    public String getBeden_kutle_endeksi() {
        return beden_kutle_endeksi;
    }

    public void setBeden_kutle_endeksi(String beden_kutle_endeksi) {
        this.beden_kutle_endeksi = beden_kutle_endeksi;
    }

    public String getKas_orani() {
        return kas_orani;
    }

    public void setKas_orani(String kas_orani) {
        this.kas_orani = kas_orani;
    }

    public String getYag_orani() {
        return yag_orani;
    }

    public void setYag_orani(String yag_orani) {
        this.yag_orani = yag_orani;
    }

    public String getSu_orani() {
        return su_orani;
    }

    public void setSu_orani(String su_orani) {
        this.su_orani = su_orani;
    }

    public String getMetabolizma_yasi() {
        return metabolizma_yasi;
    }

    public void setMetabolizma_yasi(String metabolizma_yasi) {
        this.metabolizma_yasi = metabolizma_yasi;
    }

    public String getBazal_metabolizma_hizi() {
        return bazal_metabolizma_hizi;
    }

    public void setBazal_metabolizma_hizi(String bazal_metabolizma_hizi) {
        this.bazal_metabolizma_hizi = bazal_metabolizma_hizi;
    }

    public String getEmpedans() {
        return empedans;
    }

    public void setEmpedans(String empedans) {
        this.empedans = empedans;
    }
}