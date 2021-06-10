package ch.sebug.controlrouteoptimizer.models;

import java.util.Date;

import lombok.Data;

@Data
public class MeetingImportLine {
    private Date startDate;
    private String shelterNumber;
    private String street;
    private String city;
    private String name;
    private String firstName;
}
