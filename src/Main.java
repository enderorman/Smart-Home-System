import javax.imageio.IIOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DateTimeException;

public class Main {
    public static void main(String args[]) {
        String commands[] = InputHelper.getCommands(args[0]);
        BufferedWriter out = OutputHelper.getFileWriter(args[1]);
        for (int i = 0; i < commands.length; i++) {
            String[] currentCommand = commands[i].split("\t");
            String operation = currentCommand[0];
            OutputHelper.writeCommand(out, commands[i]);
            try {
                if (i == 0){
                    ExceptionHandler.checkFirstCommand(currentCommand);
                }
                switch (operation) {
                    case "SetInitialTime":
                        CommandHelper.setInitialTime(currentCommand, out);
                        break;
                    case "SetTime":
                        CommandHelper.setTime(currentCommand);
                        break;
                    case "SkipMinutes":
                        CommandHelper.skipMinutes(currentCommand);
                        break;
                    case "Nop":
                        CommandHelper.nop();
                        break;
                    case "Add":
                        CommandHelper.addSmartDevice(currentCommand);
                        break;
                    case "Remove":
                        CommandHelper.remove(currentCommand, out);
                        break;
                    case "SetSwitchTime":
                        CommandHelper.setSwitchTimeForDevice(currentCommand);
                        break;
                    case "Switch":
                        CommandHelper.switchDevice(currentCommand);
                        break;
                    case "ChangeName":
                        CommandHelper.changeName(currentCommand);
                        break;
                    case "PlugIn":
                        CommandHelper.plugInDevice(currentCommand);
                        break;
                    case "PlugOut":
                        CommandHelper.plugOutDevice(currentCommand);
                        break;
                    case "SetKelvin":
                        CommandHelper.setKelvin(currentCommand);
                        break;
                    case "SetBrightness":
                        CommandHelper.setBrightness(currentCommand);
                        break;
                    case "SetColorCode":
                        CommandHelper.setColorCode(currentCommand);
                        break;
                    case "SetWhite":
                        CommandHelper.setWhite(currentCommand);
                        break;
                    case "SetColor":
                        CommandHelper.setColor(currentCommand);
                        break;
                    case "ZReport":
                        CommandHelper.ZReport(out);
                        break;
                    default:
                        throw new Exception("ERROR: Erroneous command!");
                }
            } catch (FirstCommandException e){
                OutputHelper.writeErrorMessage(out, e.getMessage());
                OutputHelper.closeOutput(out);
                System.exit(0);
            }
              catch (SmartSystemException e) {
                OutputHelper.writeErrorMessage(out, e.getMessage());
            } catch (DateTimeException e) {
                if (i == 0){
                    OutputHelper.writeErrorMessage(out, "ERROR: Format of the initial date is wrong! Program is going to terminate!");
                    OutputHelper.closeOutput(out);
                    System.exit(0);
                }
                OutputHelper.writeErrorMessage(out, "ERROR: Time format is not correct!");
            } catch (Exception e) {
                OutputHelper.writeErrorMessage(out, "ERROR: Erroneous command!");
            }
        }
        String[] lastCommand = commands[commands.length - 1].split("\t");
        if (!lastCommand[0].equals("ZReport")){
            OutputHelper.writeFinalZreport(out);
        }
        OutputHelper.closeOutput(out);
    }
}

