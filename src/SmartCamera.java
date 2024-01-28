import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * Updates megabytes used on switching of.
 */
public class SmartCamera extends SmartDevice{

    private double megabytesConsumedPerSecond;
    private double totalMegabytesUsed = 0;

    public SmartCamera(String name, double megabytesConsumedPerSecond) throws Exception {
        super(name, "Off");
        ExceptionHandler.checkMegabyteException(megabytesConsumedPerSecond);
        this.megabytesConsumedPerSecond = megabytesConsumedPerSecond;
    }

    public SmartCamera(String name, double megabytesConsumedPerSecond, String status) throws Exception {
        super(name, status);
        ExceptionHandler.checkMegabyteException(megabytesConsumedPerSecond);
        this.megabytesConsumedPerSecond = megabytesConsumedPerSecond;
    }

    public double getMegabytesConsumedPerSecond() {
        return megabytesConsumedPerSecond;
    }

    public void setMegabytesConsumedPerSecond(double megabytesConsumedPerSecond) throws MegabyteException {
        ExceptionHandler.checkMegabyteException(megabytesConsumedPerSecond);
        this.megabytesConsumedPerSecond = megabytesConsumedPerSecond;
    }

    @Override
    public String toString(){
        return "Smart Camera";
    }

    /**
     * If new status is off updates total megabytes used value.
     * d is seconds between previous switch and last switch time.
     * @param newSwitchStatus
     * @param time
     * @throws Exception
     */
    public void setSwitchStatus(String newSwitchStatus, LocalDateTime time) throws Exception {
        super.setSwitchStatus(newSwitchStatus, time);
        if (newSwitchStatus.equals("Off")){
            double d = 1000 * 60;
            double seconds = Duration.between(previousSwitchTime, time).toMillis() / d;
            totalMegabytesUsed += megabytesConsumedPerSecond * seconds;
        } else {
            previousSwitchTime = time;
        }
    }

    public String getDeviceInfo(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timeToSwitchStatus = (getSwitchTime() == null ? "null": getSwitchTime().format(formatter));
        String info = "Smart Camera " + name + " is " + switchStatus.toLowerCase() + " and used "
                + String.format("%.2f", totalMegabytesUsed).replace(".", ",") + " MB of storage so far (excluding current status),"
                +" and its time to switch its status is " + timeToSwitchStatus + ".";
        return info;
    }
}
