/**
 * Factory design pattern used to generate error with different messages.
 */
public class MismatchingDevicesExceptionFactory {
    public static void createMismatchingDevicesException(String deviceType, String deviceName) throws MismatchingDevicesException{
        SmartDevice device = SmartSystem.getDeviceNameGiven(deviceName);
        switch (deviceType){
            case "SmartPlug":
                if (!(device instanceof SmartPlug)){
                    throw new MismatchingDevicesException("ERROR: This device is not a smart plug!");
                }
                break;
            case  "SmartCamera":
                if(!(device instanceof SmartCamera)) {
                    throw new MismatchingDevicesException("ERROR: This device is not a smart camera!");
                }
                break;
            case "SmartLamp":
                if (!(device instanceof SmartLamp)){
                    throw new MismatchingDevicesException("ERROR: This device is not a smart lamp!");
                }
                break;
            case "SmartColorLamp":
                if (!(device instanceof SmartLampWithColor)){
                    throw new MismatchingDevicesException("ERROR: This device is not a smart color lamp!");
                }
                break;
        }
    }
}
