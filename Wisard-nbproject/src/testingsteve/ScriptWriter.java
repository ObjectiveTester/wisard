package testingsteve;

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
                sysProp = "        System.setProperty(\"webdriver.gecko.driver\", \"<path>\");\n";
                driverInit = "        WebDriver driver = new FirefoxDriver();\n";
                break;

            case "CR":
                driverImport = "import org.openqa.selenium.chrome.ChromeDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.chrome.driver\", \"<path>\");\n";
                driverInit = "        WebDriver driver = new ChromeDriver();\n";
                break;

            case "IE":
                driverImport = "import org.openqa.selenium.ie.InternetExplorerDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.ie.driver\", \"<path>\");\n";
                driverInit = "        WebDriver driver = new InternetExplorerDriver();\n";
                break;

            case "ED":
                driverImport = "import org.openqa.selenium.edge.EdgeDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.edge.driver\", \"<path>\");\n";
                driverInit = "        WebDriver driver = new EdgeDriver();\n";
                break;

            case "SA":
                driverImport = "import org.openqa.selenium.safari.SafariDriver;\n";
                driverInit = "        WebDriver driver = new SafariDriver();\n";
                break;
        }

        ui.addCode("import java.util.concurrent.TimeUnit;\n"
                + driverImport
                + "import org.openqa.selenium.JavascriptExecutor;\n"
                + "import org.openqa.selenium.WebDriver;\n"
                + "import org.openqa.selenium.WebElement;\n"
                + "import org.openqa.selenium.By;\n"
                + "import org.openqa.selenium.Alert;\n\n"
                + "public class RecordedScript {\n\n"
                + "    public static void main(String[] args) {\n"
                + "        WebElement element;\n"
                + "        Alert alert;\n"
                + sysProp
                + driverInit
                + "        JavascriptExecutor js = (JavascriptExecutor) driver;\n"
                + "        driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);\n"
                + "        driver.manage().timeouts().setScriptTimeout(300, TimeUnit.SECONDS);\n"
                + "        driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);\n"
                + "        driver.get(\"" + url + "\");\n\n"
                + "        //recorded actions//\n"
                + "        //finish//\n\n"
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
        footer = 17; //lines from the insert point to the bottom
    }

    @Override
    void writeVerifyElement(String value, String method) {
        if (value != null) {
            value = "\"" + value + "\"";
        }
        ui.insertCode("\n        //verify:" + value + "\n"
                + "        if (element.getAttribute(\"" + method + "\").contentEquals(" + value + ")) {\n"
                + "            System.out.println(\"" + method + "= \" +" + value + ");\n"
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

}
