public class FirstCommandException extends Exception{
    public FirstCommandException(){
        super("ERROR: First command must be set initial time! Program is going to terminate!");
    }
}
