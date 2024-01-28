import java.io.BufferedWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandHelper {
    public static void setInitialTime(String[] command, BufferedWriter out) throws Exception{
        if (SmartSystem.getTime() != null){
            throw new Exception("ERROR: Erroneous command!");
        }
        String date = command[1];
        SmartSystem.setTime(new Time(date));
        OutputHelper.writeTimeSetSuccessHeader(out, date);
    }

    public static void setTime(String[] command) throws Exception{
        String newDate = command[1];
        SmartSystem.getTime().setCurrentTime(newDate);
        SmartSystem.switchAllDevicesBeforeTime(SmartSystem.getTime().getCurrentTime());
    }

    public static void skipMinutes(String[] command) throws Exception{
        if (command.length != 2){
            throw new Exception("ERROR: Erroneous command!");
        }
        int min = Integer.parseInt(command[1]);
        SmartSystem.getTime().skipMinutes(min);
        SmartSystem.switchAllDevicesBeforeTime(SmartSystem.getTime().getCurrentTime());
    }

    /**
     * Sorts devices by their switch time.
     * If first device has its time for switch, sets current time to that time.
     * Switches all devices whose switch time is before current time.
     */
    public static void nop() throws Exception{
        SmartSystem.sortDevicesByTime();
        ExceptionHandler.checkNothingToSwitchException();
        LocalDateTime switchTime = SmartSystem.addedDevices.get(0).getSwitchTime();
        SmartSystem.getTime().setCurrentTime(switchTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")));
        for (SmartDevice device: SmartSystem.addedDevices){
            if (device.getSwitchTime() == null){
                continue;
            }
            if (device.getSwitchTime().isEqual(switchTime)){
                SmartSystem.switchDevice(device, switchTime);
            }
        }
    }


    public static void addSmartDevice(String[] command) throws Exception{
        String deviceType = command[1];
        switch (deviceType){
            case "SmartPlug":
                addSmartPlug(command);
                break;
            case "SmartCamera":
                addSmartCamera(command);
                break;
            case "SmartLamp":
                addSmartLamp(command);
                break;
            case "SmartColorLamp":
                addSmartColorLamp(command);
                break;
            default:
                throw new Exception("ERROR: Erroneous command!");
        }
    }

    public static void addSmartPlug(String[] command) throws Exception{
        int len = command.length;
        SmartPlug smartPlug;
        switch (len) {
            case 3:
                smartPlug = new SmartPlug(command[2]);
                SmartSystem.addedDevices.add(smartPlug);
                break;
            case 4:
                smartPlug = new SmartPlug(command[2], command[3]);
                SmartSystem.addedDevices.add(smartPlug);
                break;
            case 5:
                smartPlug = new SmartPlug(command[2], command[3], Double.parseDouble(command[4]));
                SmartSystem.addedDevices.add(smartPlug);
                break;
            default:
                throw new Exception("ERROR: Erroneous command!");
            }
        }


    public static void addSmartCamera(String[] command) throws Exception{
        int len = command.length;
        SmartCamera smartCamera;
        double megabytesConsumedPerSecond;
        switch (len) {
            case 4:
                megabytesConsumedPerSecond = Double.parseDouble(command[3]);
                smartCamera = new SmartCamera(command[2], megabytesConsumedPerSecond);
                SmartSystem.addedDevices.add(smartCamera);
                break;
            case 5:
                megabytesConsumedPerSecond = Double.parseDouble(command[3]);
                smartCamera = new SmartCamera(command[2], megabytesConsumedPerSecond, command[4]);
                SmartSystem.addedDevices.add(smartCamera);
                break;
            default:
                throw new Exception("ERROR: Erroneous command!");
        }
    }

    public static void addSmartLamp(String[] command) throws Exception{
        int len = command.length;
        SmartLamp smartLamp;
        switch (len) {
            case 3:
                smartLamp = new SmartLamp(command[2]);
                SmartSystem.addedDevices.add(smartLamp);
                break;
            case 4:
                smartLamp= new SmartLamp(command[2], command[3]);
                SmartSystem.addedDevices.add(smartLamp);
                break;
            case 6:
                smartLamp = new SmartLamp(command[2], command[3], Integer.parseInt(command[4]), Integer.parseInt(command[5]));
                SmartSystem.addedDevices.add(smartLamp);
                break;
            default:
                throw new Exception("ERROR: Erroneous command!");
            }
        }


    public static void addSmartColorLamp(String[] command) throws Exception {
        int len = command.length;
        SmartLampWithColor smartLampWithColor;
        String name, initialStatus, colorValue;
        int brightness;
        name = command[2];
        switch (len) {
            case 3:
                smartLampWithColor = new SmartLampWithColor(name);
                SmartSystem.addedDevices.add(smartLampWithColor);
                break;
            case 4:
                initialStatus = command[3];
                smartLampWithColor = new SmartLampWithColor(name, initialStatus);
                SmartSystem.addedDevices.add(smartLampWithColor);
                break;
            case 6:
                initialStatus = command[3];
                brightness = Integer.parseInt(command[5]);
                colorValue = command[4];
                smartLampWithColor = new SmartLampWithColor(name, initialStatus, colorValue, brightness);
                SmartSystem.addedDevices.add(smartLampWithColor);
                break;
            default:
                throw new Exception("ERROR: Erroneous command!");
        }
    }

    /**
     * Checks if given device name exists.
     * Removes given device and displays informatin about it.
     */
    public static void remove(String[] command, BufferedWriter out) throws Exception{
        if (command.length != 2){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1];
        ExceptionHandler.checkDeviceNotFoundException(name);
        SmartDevice device = SmartSystem.getDeviceNameGiven(name);
        OutputHelper.writeRemovalHeader(out);
        SmartSystem.removeSmartDevice(name);
        OutputHelper.writeDeviceInfo(out, device);
    }

    /**
     * Sets switch time for given device.
     */
    public static void setSwitchTimeForDevice(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1], date = command[2];
        ExceptionHandler.checkDeviceNotFoundException(name);
        SmartDevice device = SmartSystem.getDeviceNameGiven(name);
        device.setSwitchTime(date);
    }

    public static void switchDevice(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1], status = command[2];
        ExceptionHandler.checkDeviceNotFoundException(name);
        SmartDevice device = SmartSystem.getDeviceNameGiven(name);
        device.setSwitchStatus(status, SmartSystem.getTime().getCurrentTime());
    }

    public static void changeName(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String oldName = command[1], newName = command[2];
        ExceptionHandler.checkSameNameException(oldName, newName);
        ExceptionHandler.checkDeviceNotFoundException(oldName);
        ExceptionHandler.checkDeviceExistsException(newName);
        SmartDevice device = SmartSystem.getDeviceNameGiven(oldName);
        device.name = newName;
    }

    public static void plugInDevice(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1]; int ampere = Integer.parseInt(command[2]);
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartPlug", name);
        ExceptionHandler.checkAmpereException(ampere);
        SmartPlug plug = (SmartPlug) SmartSystem.getDeviceNameGiven(name);
        plug.plugIn(ampere);
    }

    public static void plugOutDevice(String[] command) throws Exception{
        if (command.length != 2){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1];
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartPlug", name);
        SmartPlug plug = (SmartPlug) SmartSystem.getDeviceNameGiven(name);
        plug.plugOut();
    }

    /**
     * Sets kelvin value for given device.
     * If device isn't smart lamp throws mismatching devices exception
     */
    public static void setKelvin(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1]; int kelvin = Integer.parseInt(command[2]);
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartLamp", name);
        SmartLamp device = (SmartLamp) SmartSystem.getDeviceNameGiven(name);
        device.setKelvin(kelvin);
    }

    /**
     * Sets brightness for given device.
     * If device isn't smart lamp throws mismatching devices exception.
     */
    public static void setBrightness(String[] command) throws Exception{
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1]; int brightness = Integer.parseInt(command[2]);
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartLamp", name);
        SmartLamp device = (SmartLamp) SmartSystem.getDeviceNameGiven(name);
        device.setBrightness(brightness);
    }

    public static void setColorCode(String[] command) throws Exception{
        /**
         * Sets color code for given device.
         * If device isn't smart color lamp throws mismatching devices exception.
         */
        if (command.length != 3){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1], colorCode = command[2];
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartColorLamp", name);
        SmartLampWithColor device = (SmartLampWithColor) SmartSystem.getDeviceNameGiven(name);
        device.setColorCode(colorCode);
    }

    /**
     * Sets given device white.
     * If device isn't smart lamp throws mismatching devices exception.
     */
    public static void setWhite(String[] command) throws Exception{
        if (command.length != 4){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1]; int kelvin = Integer.parseInt(command[2]), brightness = Integer.parseInt(command[3]);
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartLamp", name);
        SmartLamp device = (SmartLamp) SmartSystem.getDeviceNameGiven(name);
        device.setKelvin(kelvin); device.setBrightness(brightness);
    }

    /**
     * Sets a color for given device.
     * If device isn't smart color lamp throws mismatching devices exception.
     */
    public static void setColor(String[] command) throws Exception{
        if (command.length != 4){
            throw new Exception("ERROR: Erroneous command!");
        }
        String name = command[1], colorCode = command[2]; int brightness = Integer.parseInt(command[3]);
        ExceptionHandler.checkDeviceNotFoundException(name);
        ExceptionHandler.checkMismatchingDevicesException("SmartColorLamp", name);
        SmartLampWithColor device = (SmartLampWithColor) SmartSystem.getDeviceNameGiven(name);
        device.setColor(colorCode, brightness);
    }

    /**
     * Displays ZReport for all devices added to smart system.
     * Switches all devices whose switch time is before current time.
     * Sorts devices by their switch times.
     */
    public static void ZReport(BufferedWriter out) throws Exception{
        SmartSystem.switchAllDevicesBeforeTime(SmartSystem.getTime().getCurrentTime());
        SmartSystem.sortDevicesByTime();
        OutputHelper.writeZReport(out);
    }

}
