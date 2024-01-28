public class SwitchTimeException extends SmartSystemException{
    public SwitchTimeException(){
        super("ERROR: Switch time cannot be in the past!");
    }
}
