package objectivetester;

/**
 *
 * @author Steve
 */
class ScriptWriter extends DefaultWriter {
    //browsermodel calls this and it updates the generated code
    //creates a java program - e.g. for task automation

    ScriptWriter(UserInterface ui) {
        this.ui = ui;
    }

    @Override
    void writeHeader(String url, String browser) {
        String driverImport = "";
        String sysProp = "";
        String driverInit = "";

        switch (browser) {

            case "FF":
                driverImport = "import org.openqa.selenium.firefox.FirefoxDriver;\n";
                if (System.getProperty("webdriver.gecko.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.gecko.driver\", \""+System.getProperty("webdriver.gecko.driver")+"\");\n";
                }
                driverInit = "        WebDriver driver = new FirefoxDriver();\n";
                break;

            case "CR":
                driverImport = "import org.openqa.selenium.chrome.ChromeDriver;\n";
                if (System.getProperty("webdriver.chrome.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.chrome.driver\", \""+System.getProperty("webdriver.chrome.driver")+"\");\n";
                }
                driverInit = "        WebDriver driver = new ChromeDriver();\n";
                break;

            case "ED":
                driverImport = "import org.openqa.selenium.edge.EdgeDriver;\n";
                if (System.getProperty("webdriver.edge.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.edge.driver\", \""+System.getProperty("webdriver.edge.driver")+"\");\n";
                }
                driverInit = "        WebDriver driver = new EdgeDriver();\n";
                break;

            case "SA":
                driverImport = "import org.openqa.selenium.safari.SafariDriver;\n";
                driverInit = "        WebDriver driver = new SafariDriver();\n";
                break;
        }

        ui.addCode("import java.time.Duration;\n"
                + driverImport
                + "import org.openqa.selenium.JavascriptExecutor;\n"
                + "import org.openqa.selenium.WebDriver;\n"
                + "import org.openqa.selenium.WebElement;\n"
                + "import org.openqa.selenium.By;\n"
                + "import org.openqa.selenium.Alert;\n"
                + "import org.openqa.selenium.support.ui.Select;\n\n"
                + "public class RecordedScript {\n\n"
                + "    public static void main(String[] args) {\n"
                + "        WebElement element;\n"
                + "        Alert alert;\n"
                + "        Select selector;\n"
                + sysProp
                + driverInit
                + "        JavascriptExecutor js = (JavascriptExecutor) driver;\n"
                + "        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));\n"
                + "        driver.get(\"" + url + "\");\n\n\n"
                + "        driver.quit();\n"
                + "    }\n"
                + "    public static void switchWin(WebDriver driver, String title) {\n"
                + "        String target = driver.getWindowHandle();\n"
                + "        for (String handle : driver.getWindowHandles()) {\n"
                + "            driver.switchTo().window(handle);\n"
                + "            if (driver.getTitle().equals(title)) {\n"
                + "                target = handle;\n"
                + "            }\n"
                + "        }\n"
                + "        driver.switchTo().window(target);\n"
                + "    }\n"
                + "}\n");
        footer = 16; //lines from the insert point to the bottom
    }

    @Override
    void writeVerifyElement(String value, String method) {
        String check = ".contentEquals(\"" + value + "\")";
        if (value == null) {
            check = " == null";
        } else {
        
        }
        ui.insertCode("\n        //verify:" + value + "\n"
                + "        if (element.getAttribute(\"" + method + "\")" + check + ") {\n"
                + "            System.out.println(\"" + method + " = " + value + "\");\n"
                + "        }"
                + "", footer);
    }

    @Override
    void writeVerifyPage(String value) {
        if (value != null) {
            value = "\"" + value + "\"";
        }
        ui.insertCode("\n        //verify:" + value + "\n"
                + "        if (driver.getTitle().contentEquals(" + value + ")) {\n"
                + "            System.out.println(\"title= \" +" + value + ");\n"
                + "        }"
                + "", footer);
    }
    
    @Override
    void writeVerifyCookie(String name, String value) {
        ui.insertCode("\n        //verify:" + value + "\n"
                + "        if (driver.manage().getCookieNamed(\"" + name + "\").getValue().contentEquals(\"" + value + "\")) {\n"
                + "            System.out.println(\"" + name + " = " + value + "\");\n"
                + "        }"
                + "", footer);
    }
}
