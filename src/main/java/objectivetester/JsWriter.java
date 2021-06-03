package objectivetester;

/**
 *
 * @author Steve
 */
class JsWriter extends DefaultWriter {
    //browsermodel calls this and it updates the generated code
    //Webdriver.io + Mocha

    private int n = 0;

    JsWriter(UserInterface ui) {
        this.n = 0;
        this.ui = ui;
    }

    @Override
    void writeHeader(String url, String browser) {
        ui.addCode("var assert = require('assert');\n\n"
                + "describe('webdriver.io test suite', function() {\n\n"
                + "    before(function() {\n"
                + "        browser.setTimeout({\n"
                + "            'implicit', 60000);\n"
                + "            'pageLoad', 60000);\n"
                + "            'script', 60000);\n"
                + "        });\n\n"
                + "        browser.url('" + url + "');\n"
                + "    });\n\n"
                + "    after(function() {\n"
                + "    });\n\n"
                + "});\n");
        footer = 3; //lines from the insert point to the bottom

    }

    @Override
    void writeFindEvent(String method, String attribute) {
        ui.insertCode("\n        //find:" + attribute + "\n"
                + "        const element = $('" + wdioMethod(method, attribute) + "');"
                + "", footer);
    }

    @Override
    void writeClickEvent(String method, String attribute) {
        ui.insertCode("\n        //click:" + attribute + "\n"
                + "        $('" + wdioMethod(method, attribute) + "').click();"
                + "", footer);
    }

    @Override
    void writeInputEvent(String data) {
        ui.insertCode("\n        element.setValue('" + data + "');"
                + "", footer);
    }

    @Override
    void writeSelectEvent(String data) {
        ui.insertCode("\n        element.selectByVisibleText(\"" + data + "\");"
                + "", footer);
    }

    @Override
    void writeInputjsEvent(String jsInput) {
        ui.insertCode("\n        browser.executeScript(\"" + jsInput + "\");"
                + "", footer);
    }

    @Override
    void writeAlertClick(String question, boolean choice) {
        String action;
        if (choice) {
            action = "        browser.acceptAlert();\n";
        } else {
            action = "        browser.dismissAlert();\n";
        }
        ui.insertCode("\n        //alert:" + question + "\n"
                + action
                + "", footer);
    }

    @Override
    void writeSwitchByIndex(int frame) {
        ui.insertCode("\n        //switch to:" + frame + "\n"
                + "        browser.switchToFrame(" + frame + ");"
                + "", footer);
    }

    @Override
    void writeSwitchBack() {
        ui.insertCode("\n        //switch back\n"
                + "        browser.switchToParentFrame();"
                + "", footer);
    }

    @Override
    void writeSwitchWin(String title) {
        ui.insertCode("\n        //switch to:" + title + "\n"
                + "        browser.switchWindow('" + title + "');"
                + "", footer);
    }

    @Override
    void writeVerifyElement(String value, String method) {
        if (value != null) {
            value = "'" + value + "'";
        }
        ui.insertCode("\n        //assert:" + value + "\n"
                + "        assert.equal(element.getAttribute('" + method + "')," + value + ");"
                + "", footer);
    }

    @Override
    void writeVerifyPage(String value) {
        if (value != null) {
            value = "'" + value + "'";
        }
        ui.insertCode("\n        //assert:" + value + "\n"
                + "        assert.equal(browser.getTitle()," + value + ");"
                + "", footer);
    }

    @Override
    void writeVerifyCookie(String name, String value) {
        ui.insertCode("\n        //assert:" + value + "\n"
                + "        assert.equal(browser.getNamedCookie('" + name + "').value, '" + value.replaceAll("\"", "'") + "');"
                + "", footer);
    }

    @Override
    void comment(String text) {
        ui.insertCode("\n    //" + text + "\n", footer);
    }

    @Override
    void writeStart() {
        n++;
        ui.insertCode("\n    it('test " + n + "', function () {", footer);
    }

    @Override
    void writeEnd() {
        ui.insertCode("\n    });\n", footer);
    }

    String wdioMethod(String method, String attribute) {
        switch (method) {
            case "linkText":
                return "=" + attribute;
            case "name":
                return "[name=\"" + attribute + "\"]";
            case "id":
                return "#" + attribute;
            case "cssSelector":
                return attribute.replaceAll("'", "\"");
            case "className":
                return "." + attribute;
            default:
                return attribute;
        }
    }
}
