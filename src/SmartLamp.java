import java.time.format.DateTimeFormatter;

/**
 * Smart lamp has kelvin and brightness values.
 */
public class SmartLamp extends SmartDevice{
    /**
     * Kelvin value must be in range 2000, 6500.
     * Brightness must be in range 0%-100%;
     */
    private int kelvin, brightness;
    public SmartLamp(String name) throws Exception {
        super(name, "Off");
        this.kelvin = 4000;
        this.brightness = 100;
    }

    public SmartLamp(String name, String status) throws Exception{
        super(name, status);
        this.kelvin = 4000;
        this.brightness = 100;
    }

    public SmartLamp(String name, String status, int kelvin, int brightness) throws Exception{
        super(name, status);
        ExceptionHandler.checkKelvinException(kelvin);
        ExceptionHandler.checkBrightnessException(brightness);
        this.kelvin = kelvin;
        this.brightness = brightness;
    }


    public int getKelvin() {
        return kelvin;
    }

    public void setKelvin(int kelvin) throws KelvinException {
        ExceptionHandler.checkKelvinException(kelvin);
        this.kelvin = kelvin;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) throws BrightnessException{
        ExceptionHandler.checkBrightnessException(brightness);
        this.brightness = brightness;
    }

    @Override
    public String toString(){
        return "Smart Lamp";
    }

    public String getDeviceInfo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timeToSwitchStatus = (getSwitchTime() == null ? "null": getSwitchTime().format(formatter));
        String info = "Smart Lamp " + name + " is " + switchStatus.toLowerCase() + " and its kelvin value is "
                + Integer.toString(kelvin) + "K with " + Integer.toString(brightness) + "% brightness,"
                +" and its time to switch its status is " + timeToSwitchStatus + ".";
        return info;
    }
}
