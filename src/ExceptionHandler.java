import java.time.LocalDateTime;

/**
 * Class with static methods to handle exceptions.
 */
public class ExceptionHandler{
    public static void checkDeviceExistsException(String name) throws DeviceExistsException{
        for (SmartDevice smartDevice: SmartSystem.addedDevices){
            String deviceName = smartDevice.name;
            if (name.equals(deviceName)){
                throw new DeviceExistsException("ERROR: There is already a smart device with same name!");
            }
        }
    }
    public static void checkDeviceNotFoundException(String name) throws DeviceNotFoundException{
        if (SmartSystem.getDeviceNameGiven(name) == null){
            throw new DeviceNotFoundException("ERROR: There is not such a device!");
        }
    }

    public static void checkSameNameException(String oldName, String newName) throws SameNameException{
        if (oldName.equals(newName)){
            throw new SameNameException("ERROR: Both of the names are the same, nothing changed!");
        }
    }
    public static void checkAmpereException(double ampere) throws AmpereException{
        if (ampere <= 0){
            throw new AmpereException("ERROR: Ampere value must be a positive number!");
        }
    }

    public static void checkMegabyteException(double megabyte) throws MegabyteException{
        if (megabyte <= 0){
            throw new MegabyteException("ERROR: Megabyte value must be a positive number!");
        }
    }

    public static void checkKelvinException(int kelvin) throws KelvinException{
        if (kelvin < 2000 || kelvin > 6500){
            throw new KelvinException("ERROR: Kelvin value must be in range of 2000K-6500K!");
        }
    }

    public static void checkBrightnessException(int brightness) throws BrightnessException{
        if (brightness < 0 || brightness > 100){
            throw new BrightnessException("ERROR: Brightness must be in range of 0%-100%!");
        }
    }

    public static void checkColorCodeException(String colorCode) throws ColorCodeException{
        int hex = Integer.decode(colorCode);
        int st = 0x000000, end = 0xFFFFFF;
        if (hex < st || hex > end){
            throw new ColorCodeException("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
        }
    }

    public static void checkPlugException(String name, boolean plugIn) throws PlugException {
        SmartPlug plug = (SmartPlug) SmartSystem.getDeviceNameGiven(name);
        if (plugIn && plug.plugStatus.equals("Plugged")){
            throw new PlugException("ERROR: There is already an item plugged in to that plug!");
        }
        if (!plugIn && plug.plugStatus.equals("Unplugged")){
            throw new PlugException("ERROR: This plug has no item to plug out from that plug!");
        }
    }

    public static void checkMismatchingDevicesException(String deviceType, String deviceName) throws MismatchingDevicesException{
        MismatchingDevicesExceptionFactory.createMismatchingDevicesException(deviceType, deviceName);
    }

    public static void checkNothingToSwitchException() throws Exception {
        if (SmartSystem.addedDevices.size() == 0 || SmartSystem.addedDevices.get(0).getSwitchTime() == null){
            throw new NothingToSwitchException();
        }
    }

    public static void checkSwitchException(String currentStatus, String newSwitchStatus) throws SwitchException{
        if (currentStatus.equals("On") && newSwitchStatus.equals("On")){
            throw new SwitchException("ERROR: This device is already switched on!");
        }

        else if(currentStatus.equals("Off") && newSwitchStatus.equals("Off")){
            throw new SwitchException("ERROR: This device is already switched off!");
        }
    }

    public static void checkTimeReverseError(LocalDateTime newTime) throws TimeReverseException{
        if (newTime.isBefore(SmartSystem.getTime().getCurrentTime())){
            throw new TimeReverseException();
        }
    }

    public static void checkFirstCommand(String[] command) throws Exception{
        if (!command[0].equals("SetInitialTime") || command.length != 2){
            throw new FirstCommandException();
        }
    }

    public static void checkSameTimeException(LocalDateTime newTime) throws SameTimeException{
        if (SmartSystem.getTime().getCurrentTime().isEqual(newTime)){
            throw new SameTimeException();
        }
    }

    public static void checkSwitchTimeException(LocalDateTime newSwitchTime) throws SwitchTimeException{
        if (newSwitchTime.isBefore(SmartSystem.getTime().getCurrentTime())){
            throw new SwitchTimeException();
        }
    }
}
