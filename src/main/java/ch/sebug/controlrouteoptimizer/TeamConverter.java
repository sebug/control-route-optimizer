package ch.sebug.controlrouteoptimizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.util.List;
import ch.sebug.controlrouteoptimizer.models.Team;

public class TeamConverter implements Converter {
    private List<Team> teams;

    public TeamConverter(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (value == null) {
            return null;
        }
        for (Team t : teams) {
            if (value.equals(t.getName())) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) throws ConverterException {
        if (o instanceof Team) {
            Team t = (Team)o;
            return t.getName();
        }
        return null;
    }
}
