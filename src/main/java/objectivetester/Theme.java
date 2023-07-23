package objectivetester;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public final class Theme 
{
    public static boolean light() {
        Boolean light = false;
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("windows")) {
            if (Advapi32Util.registryGetValue(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Themes\\personalize", "AppsUseLightTheme").toString().contentEquals("1")) {
                light = true;
            } 
        } else if (os.contains("mac")) {
            try {
                Runtime rt = Runtime.getRuntime();
                String[] commands = {"/usr/bin/defaults", "read", "-g", "AppleInterfaceStyle"};
                Process proc = rt.exec(commands);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                String s = null;
                while ((s = stdError.readLine()) != null) {
                    light = true;
                }
    
                s = null;
                while ((s = stdInput.readLine()) != null) {
                    if (s.equalsIgnoreCase("dark")) {
                        light = false;
                    }
    
                }
            } catch (Exception e) {
                //System.out.println(e);
            }
        }

        return light;
    }

}
