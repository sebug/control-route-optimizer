package ch.sebug.controlrouteoptimizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.util.List;
import ch.sebug.controlrouteoptimizer.models.Shelter;

public class ShelterConverter implements Converter {
    private List<Shelter> shelters;

    public ShelterConverter(List<Shelter> shelters) {
        this.shelters = shelters;
    }

    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) throws ConverterException {
        if (value == null) {
            return null;
        }
        for (Shelter s : shelters) {
            if (value.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object o) throws ConverterException {
        if (o instanceof Shelter) {
            Shelter s = (Shelter)o;
            return s.getName();
        }
        return null;
    }
}
