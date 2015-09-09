package maunaloa.converters;

import javafx.scene.paint.Color;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by rcs on 09.09.15.
 */
public class StringToColorConverter implements Converter<String, Color> {
    @Override
    public Color convert(String source) {
        return Color.web(source);
    }
}
