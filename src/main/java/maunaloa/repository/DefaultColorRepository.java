package maunaloa.repository;

import javafx.scene.paint.Color;
import oahux.repository.ColorReposEnum;
import oahux.repository.ColorRepository;

/**
 * Created by rcs on 09.09.15.
 *
 */
public class DefaultColorRepository implements ColorRepository {
    // BACKGROUND, FOREGROUND, VOLUME;
    private Color background = Color.WHITE;
    private Color foreground = Color.BLACK;
    private Color cycle10 = Color.RED;
    private Color cycle50 = Color.RED;
    private Color cycle200 = Color.RED;
    private Color volume = Color.RED;
    private Color candlesticksBear = Color.RED;

    //region Interface Methods 
    @Override
    public Color colorFor(ColorReposEnum c) {
        switch (c) {
            case BACKGROUND: return background;
            case FOREGROUND: return foreground;
            case VOLUME: return foreground;
            case CNDL_BEAR: return foreground;
            default: return Color.WHITE;
        }
    }
    @Override
    public Color colorForCycle(int days) {
        switch (days) {
            case 10: return cycle10;
            case 50: return cycle50;
            case 200: return cycle200;
            default: return Color.RED;
        }
    }

    /*
    public <Integer> Color colorFor2(ColorReposEnum c, Integer param) {
        return null;
    }
    */
    //endregion Interface Methods 

    //region Properties
    public void setBackground(Color color) {
        background = color; 
    }
    public void setForeground(Color color) {
        foreground = color; 
    }
    public void setVolume(Color color) {
        volume = color; 
    }
    public void setCycle10(Color color) {
        cycle10 = color; 
    }
    public void setCycle50(Color color) {
        cycle50 = color; 
    }
    public void setCycle200(Color color) {
        cycle200 = color; 
    }
    public void setCandlesticksBear(Color color) {
        candlesticksBear = color; 
    }

    //endregion Properties
}
