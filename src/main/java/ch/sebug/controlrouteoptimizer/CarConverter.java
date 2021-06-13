package ch.sebug.controlrouteoptimizer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.util.List;
import ch.sebug.controlrouteoptimizer.models.Car;

public class CarConverter implements Converter {
    private List<Car> cars;

    public CarConverter(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (value == null) {
            return null;
        }
        for (Car c : cars) {
            if (value.equals(c.getName())) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) throws ConverterException {
        if (o instanceof Car) {
            Car c = (Car)o;
            return c.getName();
        }
        return null;
    }
    
}
