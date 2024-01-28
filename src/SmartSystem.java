import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

public class SmartSystem {
    static ArrayList<SmartDevice> addedDevices = new ArrayList<SmartDevice>();
    private static Time time;

    /**
     * Sort devices by using bubble by their switch time.
     * Null switch times must be at the end of added devices.
     * @throws Exception
     */
    public static void sortDevicesByTime() throws Exception{
        int len = addedDevices.size();
        for (int i = 0; i < len; i++){
            for (int j = 0; j + 1 < len; j++){
                LocalDateTime switchTime1 = addedDevices.get(j).getSwitchTime();
                LocalDateTime switchTime2 = addedDevices.get(j + 1).getSwitchTime();
                if (switchTime2 != null && (switchTime1 == null || switchTime1.isAfter(switchTime2))){
                    Collections.swap(addedDevices, j,j + 1);
                }
            }
        }
    }

    /**
     * Switches all devices whose switch time is before given time.
     * Respects initial order of devices while sorting them.
     * Sorts devices after every switch operation.
     * @param time
     * @throws Exception
     */
    public static void switchAllDevicesBeforeTime(LocalDateTime time) throws Exception{
        ArrayList<LocalDateTime> switchTimes = new ArrayList<>();

        for (SmartDevice device: addedDevices){
            if (device.getSwitchTime() == null){
                continue;
            }
            switchTimes.add(device.getSwitchTime());
        }

        for (LocalDateTime switchTime: switchTimes){
            for (SmartDevice device: addedDevices){
                if (device.getSwitchTime() == null){
                    continue;
                }
                if (switchTime.isEqual(device.getSwitchTime())){
                    switchDevice(device, time);
                }
            }
            sortDevicesByTime();
        }
    }

    /**
     * Switches given device if its switch time is before time.
     * @param device
     * @param time
     * @throws Exception
     */
    public static void switchDevice(SmartDevice device, LocalDateTime time) throws Exception{
        LocalDateTime deviceSwitchTime = device.getSwitchTime();
        if (deviceSwitchTime != null && (deviceSwitchTime.isBefore(time) || deviceSwitchTime.isEqual(time))){
            if (device.switchStatus.equals("On")){
                device.setSwitchStatus("Off", deviceSwitchTime);
            }  else{
                device.setSwitchStatus("On", deviceSwitchTime);
            }
            device.setSwitchTime(null);
        }
    }

    /**
     * If device exists fetchs it from SmartSystem
     * @param name
     * @return device
     */
    public static SmartDevice getDeviceNameGiven(String name){
        int len = addedDevices.size();
        for (int i = 0; i < len; i++){
            SmartDevice device = addedDevices.get(i);
            if (device.name.equals(name)){
                return device;
            }
        }
        return null;
    }

    /**
     * Removes device from smart system.
     * Switches off device if it's on.
     * @param name
     * @throws Exception
     */
    public static void removeSmartDevice(String name) throws Exception{
        int len = SmartSystem.addedDevices.size();
        SmartDevice device;
        for (int i = 0; i < len; i++){
            device = SmartSystem.addedDevices.get(i);
            if (device.name.equals(name)){
                SmartSystem.addedDevices.remove(i);
                if(device.switchStatus.equals("On")){
                    device.setSwitchStatus("Off", SmartSystem.getTime().getCurrentTime());
                }
                break;
            }
        }
    }


    public static Time getTime() {
        return time;
    }

    public static void setTime(Time time) {
        SmartSystem.time = time;
    }
}
