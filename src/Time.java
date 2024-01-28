import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores current time as LocalDateTime object.
 */
public class Time {
    private LocalDateTime currentTime;
    public Time(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        currentTime = LocalDateTime.parse(date, formatter);
    }

    public LocalDateTime getCurrentTime(){
        return currentTime;
    }

    public void setCurrentTime(String newDate) throws TimeReverseException, SameTimeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        LocalDateTime newTime = LocalDateTime.parse(newDate, formatter);
        ExceptionHandler.checkTimeReverseError(newTime);
        ExceptionHandler.checkSameTimeException(newTime);
        currentTime = newTime;
    }

    /**
     * Checks if min is greater than 0.
     * Updates time given min later.
     * @param min
     * @throws Exception
     */
    public void skipMinutes(int min) throws Exception{
        if (min < 0){
            throw new TimeReverseException();
        }
        if (min == 0){
            throw new NothingToSkipException();
        }
        currentTime = currentTime.plusMinutes(min);
    }
}
