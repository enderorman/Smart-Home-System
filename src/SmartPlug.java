import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Updates energy consumption when it's switched off or plugged off.
 */
public class SmartPlug extends SmartDevice{
    double voltage = 220, ampere = 0;
    private double energyConsumption = 0;
    String plugStatus = "Unplugged";

    public SmartPlug(String name) throws Exception {
        super(name, "Off");
    }

    public SmartPlug(String name, String status) throws Exception {
        super(name, status);
    }

    public SmartPlug(String name, String status, double ampere) throws Exception{
        super(name, status);
        ExceptionHandler.checkAmpereException(ampere);
        this.ampere = ampere;
    }

    @Override
    public void setSwitchStatus(String newSwitchStatus, LocalDateTime time) throws Exception {
        super.setSwitchStatus(newSwitchStatus, time);
        if (newSwitchStatus.equals("Off")){
            updateEnergyConsumption(time);
        }
        else if(plugStatus.equals("Plugged")){
            previousSwitchTime = time;
        }
    }

    /**
     * Energy Consumed = Voltage * Ampere * Hours
     * Hours = Hour between previous switch time and last switch time.
     * @param time
     */
    public void updateEnergyConsumption(LocalDateTime time){
        double d = 1000 * 60 * 60;
        double hours = Duration.between(previousSwitchTime, time).toMillis() / d;
        energyConsumption += voltage * ampere * hours;
    }

    /**
     * Updates. previous switch time.
     * @param ampere
     * @throws PlugException
     */
    public void plugIn(int ampere) throws PlugException{
        boolean plugIn = true;
        ExceptionHandler.checkPlugException(name, plugIn);
        if (switchStatus.equals("On")){
            previousSwitchTime = SmartSystem.getTime().getCurrentTime();
        }
        plugStatus = "Plugged";
        this.ampere = ampere;
    }

    /**
     * If plug is switched updates energy consumption.
     * @throws PlugException
     */
    public void plugOut() throws PlugException{
        boolean plugIn = false;
        ExceptionHandler.checkPlugException(name, plugIn);
        if (switchStatus.equals("On")){
            updateEnergyConsumption(SmartSystem.getTime().getCurrentTime());
        }
        ampere = 0;
        plugStatus = "Unplugged";
    }

    @Override
    public String toString(){
        return "Smart Plug";
    }

    public String getDeviceInfo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timeToSwitchStatus = (getSwitchTime() == null ? "null": getSwitchTime().format(formatter));
        String info = "Smart Plug " + name + " is " + switchStatus.toLowerCase() + " and consumed "
                + String.format("%.2f", energyConsumption).replace(".", ",") + "W so far (excluding current device),"
                +" and its time to switch its status is " + timeToSwitchStatus + ".";
        return info;
    }

}
