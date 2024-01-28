import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OutputHelper {
    public static BufferedWriter getFileWriter(String filePath) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            return out;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeErrorMessage(BufferedWriter out, String message) {
        try {
            out.write(message);
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeCommand(BufferedWriter out, String command) {
        try {
            out.write(String.format("COMMAND: %s", command));
            out.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeRemovalHeader(BufferedWriter out) {
        try {
            out.write("SUCCESS: Information about removed smart device is as follows:");
            out.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeDeviceInfo(BufferedWriter out, SmartDevice device) {
        try {
            out.write(device.getDeviceInfo());
            out.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeTimeSetSuccessHeader(BufferedWriter out, String time) {
        try {
            out.write(String.format("SUCCESS: Time has been set to %s!", time));
            out.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeZReport(BufferedWriter out) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
            String currentTime = SmartSystem.getTime().getCurrentTime().format(formatter);
            out.write(String.format("Time is:\t%s", currentTime));
            out.newLine();
            for (SmartDevice device: SmartSystem.addedDevices){
                out.write(device.getDeviceInfo());
                out.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFinalZreport(BufferedWriter out){
        try {
            out.write("ZReport:\n");
            writeZReport(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeOutput(BufferedWriter out){
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
