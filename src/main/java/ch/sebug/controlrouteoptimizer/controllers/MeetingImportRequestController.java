package ch.sebug.controlrouteoptimizer.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.ShelterConverter;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.MeetingImportLine;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.models.TimeSlot;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;
import ch.sebug.controlrouteoptimizer.services.MapService;

@Scope(value = "session")
@Component(value = "meetingImportRequestController")
@ELBeanName(value = "meetingImportRequestController")
@Join(path = "/meetingImportRequest", to = "/meetingImportRequest-form.jsf")
public class MeetingImportRequestController {
    private final ShelterRepository shelterRepository;
    private final TimeSlotRepository timeSlotRepository;

    public MeetingImportRequestController(ShelterRepository shelterRepository,
    TimeSlotRepository timeSlotRepository) {
        this.shelterRepository = shelterRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    private UploadedFile meetingFile;
    public UploadedFile getMeetingFile() {
        return meetingFile;
    }

    public void setMeetingFile(UploadedFile meetingFile) {
        this.meetingFile = meetingFile;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        this.setMeetingFile(event.getFile());
        importFileBytes = event.getFile().getInputStream().readAllBytes();
    }

    private byte[] importFileBytes;

    public String importFile() throws IOException {
        InputStream excelInputStream = new ByteArrayInputStream(importFileBytes);
        Workbook workbook = new XSSFWorkbook(excelInputStream);
        importMeetings(workbook.getSheetAt(0));

        System.out.println(workbook);
        return "/shelter-list.xhtml?faces-redirect=true";
    }

    private void importMeetings(Sheet sheet) {
        int idx = 0;
        ArrayList<MeetingImportLine> importLines = new ArrayList<>();
        for (Row row : sheet) {
            if (idx == 0) {
                idx += 1;
                continue;
            }
            idx += 1;
            int j = 0;
            MeetingImportLine importLine = new MeetingImportLine();
            for (Cell c : row) {
                if (j == 1) {
                    importLine.setStartDate(c.getDateCellValue());
                } else if (j == 5) {
                    importLine.setShelterNumber("" + ((int)c.getNumericCellValue()));
                } else if (j == 6) {
                    importLine.setStreet(c.getStringCellValue());
                } else if (j == 7) {
                    importLine.setCity(c.getStringCellValue());
                } else if (j == 9) {
                    if (c.getCellType() == CellType.STRING) {
                        importLine.setName(c.getStringCellValue());
                    }
                } else if (j == 10) {
                    if (c.getCellType() == CellType.STRING) {
                        importLine.setFirstName(c.getStringCellValue());
                    }
                }
                j += 1;
            }
            if (importLine.getStartDate() != null) {
                importLines.add(importLine);
            }
        }
        importMeetingImportLines(importLines);
    }

    private void importMeetingImportLines(List<MeetingImportLine> importLines) {
        importLines = importLines.stream().sorted(new Comparator<MeetingImportLine>(){

            @Override
            public int compare(MeetingImportLine o1, MeetingImportLine o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
            
        }).collect(java.util.stream.Collectors.toList());
        for (MeetingImportLine importLine : importLines) {
            TimeSlot exampleTimeSlot = new TimeSlot();
            exampleTimeSlot.setStartDate(importLine.getStartDate());
            Optional<TimeSlot> timeSlotSearchResult = this.timeSlotRepository.findOne(Example.of(exampleTimeSlot));
            TimeSlot timeSlot;
            if (timeSlotSearchResult.isPresent()) {
                timeSlot = timeSlotSearchResult.get();
            } else {
                timeSlot = new TimeSlot();
                timeSlot.setStartDate(importLine.getStartDate());
                timeSlot = this.timeSlotRepository.save(timeSlot);
                System.out.println(timeSlot);
            }
        }
    }
}
