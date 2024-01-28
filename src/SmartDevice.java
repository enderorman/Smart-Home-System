import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for all devices.
 * Updates previous switch time on switching on.
 */
public abstract class SmartDevice {
    private LocalDateTime switchTime;
    protected LocalDateTime previousSwitchTime;
    String name, switchStatus;

    public SmartDevice(String name, String status) throws Exception {
        if (!(status.equals("On") || status.equals("Off"))){
            throw new Exception("ERROR: Erroneous command!");
        }
        ExceptionHandler.checkDeviceExistsException(name);
        this.name = name;
        this.switchStatus = status;
        if (status.equals("On")){
            previousSwitchTime = SmartSystem.getTime().getCurrentTime();
        }
    }

    protected LocalDateTime getSwitchTime() {
        return switchTime;
    }

    protected void setSwitchTime(String date) throws Exception {
        if (date == null){
            switchTime = null;
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime newSwitchTime = LocalDateTime.parse(date, formatter);
        ExceptionHandler.checkSwitchTimeException(newSwitchTime);
        switchTime = newSwitchTime;
    }

    protected void setSwitchStatus(String newSwitchStatus, LocalDateTime time) throws Exception {
        if (!(newSwitchStatus.equals("On") || newSwitchStatus.equals("Off"))){
            throw new Exception("ERROR: Erroneous command!");
        }
        ExceptionHandler.checkSwitchException(switchStatus, newSwitchStatus);
        switchStatus = newSwitchStatus;
        if (newSwitchStatus.equals("On")){
            previousSwitchTime = SmartSystem.getTime().getCurrentTime();
        }
    }

    abstract public String getDeviceInfo();

}
