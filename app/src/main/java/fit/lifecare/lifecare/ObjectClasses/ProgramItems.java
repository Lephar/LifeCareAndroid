package fit.lifecare.lifecare.ObjectClasses;


import java.io.Serializable;

import fit.lifecare.lifecare.DatabaseClasses.ProgramlarimData;

public class ProgramItems implements Serializable {

    private String Date;
    private String ProgramName;
    private String DietitianName;
    private String Type;
    ProgramlarimData Program_data;
    ProgramlarimData[] Program_data_daily;
    
    public ProgramItems(String date, String programName, String dietitianName, String type, ProgramlarimData program_data, ProgramlarimData[] program_data_daily) {
        Date = date;
        ProgramName = programName;
        DietitianName = dietitianName;
        Type = type;
        Program_data = program_data;
        Program_data_daily = program_data_daily;
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
    
    public ProgramlarimData getProgram_data() {
        return Program_data;
    }
    
    public void setProgram_data(ProgramlarimData program_data) {
        Program_data = program_data;
    }
    
    public ProgramlarimData[] getProgram_data_daily() {
        return Program_data_daily;
    }
    
    public void setProgram_data_daily(ProgramlarimData[] program_data_daily) {
        Program_data_daily = program_data_daily;
    }
}
