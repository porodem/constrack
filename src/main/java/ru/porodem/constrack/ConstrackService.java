package ru.porodem.constrack;

/**
 * Предоставляет вспомогательные методы которые могут использоваться любыи классами
 * @author porod
 *
 */
public class ConstrackService {
	
	/**
	*check if user input only numbers for RUB value
	*/
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
    
  /**
   * for debbug. Show logging messages.
   * @param msg
   */
    public static void showLog(String msg) {
    	System.out.println("Log: " + msg);
    }
}
