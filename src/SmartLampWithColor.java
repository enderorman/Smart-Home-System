import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * It has color code attribute different than smart lamp.
 */
public class SmartLampWithColor extends SmartLamp{
    /**
     * Color code can have hexadecimal value and decimal value which is kelvin
     */
    private String colorCode;
    public SmartLampWithColor(String name) throws Exception {
        super(name);
        this.colorCode = "4000K";
    }

    public SmartLampWithColor(String name, String status) throws Exception {
        super(name, status);
        this.colorCode = "4000K";
    }


    public SmartLampWithColor(String name, String status, String colorCode, int brightness) throws Exception {
        super(name, status);
        setBrightness(brightness);
        if (colorCode.startsWith("0x")){
            ExceptionHandler.checkColorCodeException(colorCode);
            this.colorCode= colorCode;
        } else{
            ExceptionHandler.checkKelvinException(Integer.parseInt(colorCode));
            this.colorCode = colorCode + "K";
        }
    }

    public String getColorCode() {
        return colorCode;
    }



    @Override
    public void setKelvin(int kelvin) throws KelvinException{
        ExceptionHandler.checkKelvinException(kelvin);
        this.colorCode = Integer.toString(kelvin) + "K";
    }

    public void setColorCode(String colorCode) throws ColorCodeException, KelvinException {
        if (colorCode.startsWith("0x")){
            ExceptionHandler.checkColorCodeException(colorCode);
            this.colorCode = colorCode;
        } else{
            ExceptionHandler.checkKelvinException(Integer.parseInt(colorCode));
            this.colorCode = colorCode + "K";
        }
    }

    public void setColor(String colorCode, int brightness) throws Exception{
        ExceptionHandler.checkBrightnessException(brightness);
        setColorCode(colorCode);
    }

    @Override
    public String toString(){
        return "Smart Color Lamp";
    }

    public String getDeviceInfo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timeToSwitchStatus = (getSwitchTime() == null ? "null": getSwitchTime().format(formatter));
        String info = "Smart Color Lamp " + name + " is " + switchStatus.toLowerCase() + " and its color value is "
                + colorCode + " with " + Integer.toString(getBrightness()) + "% brightness,"
                +" and its time to switch its status is " + timeToSwitchStatus + ".";
        return info;
    }

}
