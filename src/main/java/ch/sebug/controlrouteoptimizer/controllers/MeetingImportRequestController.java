package ch.sebug.controlrouteoptimizer.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
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
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.ShelterConverter;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.MeetingImportLine;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
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
                }
                j += 1;
            }
            if (importLine.getStartDate() != null) {
                importLines.add(importLine);
            }
        }
        System.out.println("Meeting import lines length " + importLines.size());
    }
}
