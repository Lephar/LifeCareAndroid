package fit.lifecare.lifecare.ObjectClasses;


import java.io.Serializable;

public class ProgramItems implements Serializable {

    private String Date;
    private String ProgramName;
    private String DietitianName;
    private String Type;
    private Integer Index;

    public ProgramItems(String date, String programName, String dietitianName, String type, Integer index) {
        Date = date;
        ProgramName = programName;
        DietitianName = dietitianName;
        Type = type;
        Index = index;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getProgramName() {
        return ProgramName;
    }

    public void setProgramName(String programName) {
        ProgramName = programName;
    }

    public String getDietitianName() {
        return DietitianName;
    }

    public void setDietitianName(String dietitianName) {
        DietitianName = dietitianName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Integer getIndex() {
        return Index;
    }

    public void setIndex(Integer index) {
        Index = index;
    }
}
