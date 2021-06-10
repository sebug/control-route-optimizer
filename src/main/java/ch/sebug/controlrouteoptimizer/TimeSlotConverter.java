package ch.sebug.controlrouteoptimizer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import ch.sebug.controlrouteoptimizer.models.TimeSlot;

public class TimeSlotConverter implements Converter {
    private List<TimeSlot> timeSlots;

    public TimeSlotConverter(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) throws ConverterException {
        if (value == null || value == "") {
            return null;
        }
        Date dateToSearch;
        try {
            dateToSearch = DateFormat.getDateTimeInstance().parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        if (dateToSearch == null) {
            return null;
        }
        for (TimeSlot t : timeSlots) {
            
            if (dateToSearch.equals(t.getStartDate())) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object o) throws ConverterException {
        if (o instanceof TimeSlot) {
            TimeSlot t = (TimeSlot)o;
            return DateFormat.getDateTimeInstance().format(t.getStartDate());
        }
        return null;
    }
}
