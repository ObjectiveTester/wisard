package objectivetester;

/**
 *
 * @author steve
 */
public class DefaultWriter {
    //browsermodel calls this and it updates the generated code

    UserInterface ui;
    int footer = 0;

    void writeHeader(String url, String browser) {
    }

    void writeFindEvent(String method, String attribute) {
        ui.insertCode("\n        //find:" + attribute + "\n"
                + "        element = driver.findElement(By." + method + "(\"" + attribute + "\"));"
                + "", footer);
    }

    void writeClickEvent(String method, String attribute) {
        ui.insertCode("\n        //click:" + attribute + "\n"
                + "        driver.findElement(By." + method + "(\"" + attribute + "\")).click();"
                + "", footer);
    }

    void writeInputEvent(String data) {
        ui.insertCode("\n        element.clear();\n"
                + "        element.sendKeys(\"" + data + "\");"
                + "", footer);
    }

    void writeSelectEvent(String data) {
        ui.insertCode("\n        selector = new Select(element);\n"
                + "        selector.selectByVisibleText(\"" + data + "\");"
                + "", footer);
    }

    void writeInputjsEvent(String jsInput) {
        ui.insertCode("\n        js.executeScript(\"" + jsInput + "\");"
                + "", footer);
    }

    void writeAlertClick(String question, boolean choice) {
        String action;
        if (choice) {
            action = "        alert.accept();\n";
        } else {
            action = "        alert.dismiss();\n";
        }
        ui.insertCode("\n        //alert:" + question + "\n"
                + "        alert = driver.switchTo().alert();\n"
                + action
                + "", footer);
    }
    
    void writeSwitchByIndex(int frame) {
        ui.insertCode("\n        //switch to:" + frame + "\n"
                + "        driver.switchTo().frame(" + frame + ");"
                + "", footer);
    }

    void writeSwitchBack() {
        ui.insertCode("\n        //switch back\n"
                + "        driver.switchTo().defaultContent();"
                + "", footer);
    }

    void writeSwitchWin(String title) {
        ui.insertCode("\n        //switch to:" + title + "\n"
                + "        switchWin(driver, \"" + title + "\");"
                + "", footer);
    }

    void writeVerifyElement(String value, String method) {
        if (value != null) {
            value = "\"" + value + "\"";
        }
        ui.insertCode("\n        //assert:" + value + "\n"
                + "        assertEquals(" + value + ",element.getAttribute(\"" + method + "\"));"
                + "", footer);
    }

    void writeVerifyPage(String value) {
        if (value != null) {
            value = "\"" + value + "\"";
        }
        ui.insertCode("\n        //assert:" + value + "\n"
                + "        assertEquals(" + value + ", driver.getTitle());"
                + "", footer);
    }
    
    void writeVerifyCookie(String name, String value) {
                ui.insertCode("\n        //assert:" + value + "\n"
                + "        assertEquals(\"" + value + "\", driver.manage().getCookieNamed(\"" + name + "\").getValue().toString());"
                + "", footer);
    }
    
    void comment(String text) {
        ui.insertCode("\n    //" + text + "\n", footer);
    }

    void writeStart() {
    }

    void writeEnd() {
    }
}
