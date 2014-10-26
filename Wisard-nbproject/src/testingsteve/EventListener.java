package testingsteve;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.openqa.selenium.WebElement;

/**
 *
 * @author Steve
 */
class EventListener extends MouseAdapter implements java.awt.event.ActionListener {

    JPopupMenu popup;
    JTable table;
    BrowserDriver bd;
    int current;

    EventListener(JPopupMenu popup, JTable table, BrowserDriver bd) {
        this.popup = popup;
        this.table = table;
        this.bd = bd;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (bd.safe) {
            //dont allow unsafe interactions until the examiner has finished
            clickEvent(e);
            popupEvent(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (bd.safe) {
            //dont allow unsafe interactions until the examiner has finished
            popupEvent(e);
        }
    }

    private void popupEvent(MouseEvent e) {
        if (e.isPopupTrigger()) {
            //reenable all choices
            popup.getComponent(0).setEnabled(true);   //click
            popup.getComponent(1).setEnabled(true);   //find
            popup.getComponent(2).setEnabled(true);   //verify
            popup.getComponent(3).setEnabled(true);   //assert
            popup.getComponent(4).setEnabled(true);   //identify

            int idx = table.rowAtPoint(e.getPoint());
            table.getSelectionModel().setSelectionInterval(idx, idx);
            current = table.getSelectedRow();

            //disable the invalid choices
            if ((table.getValueAt(current, 0).toString().contentEquals(Const.TITLE))) {
                //cant find or ident a page, so grey it out
                popup.getComponent(1).setEnabled(false);
                popup.getComponent(4).setEnabled(false);
                if (!(table.getValueAt(current, 1).toString().contentEquals(Const.CURRENT))) {
                    //cant verify other pages
                    popup.getComponent(2).setEnabled(false);
                    popup.getComponent(3).setEnabled(false);
                } else {
                    //cant click on current page
                    popup.getComponent(0).setEnabled(false);
                }
            }
            if (table.getValueAt(current, 0).toString().contains("form") && !table.getValueAt(current, 0).toString().contains(":")) {
                //can only verify form elements, not forms
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(2).setEnabled(false);
                popup.getComponent(3).setEnabled(false);
            }
            if ((table.getValueAt(current, 0).toString().endsWith(":hidden"))) {
                //cant click or id hidden form fields
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(4).setEnabled(false);
            }
            if ((table.getValueAt(current, 1).toString().contains(Const.INVISIBLE))) {
                //cant click or id invisible objects
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(4).setEnabled(false);
            }
            //show the popup menu
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void clickEvent(MouseEvent e) {
        int idx = table.rowAtPoint(e.getPoint());
        table.getSelectionModel().setSelectionInterval(idx, idx);
        current = table.getSelectedRow();
        String element = table.getModel().getValueAt(current, 0).toString();
        String location = table.getModel().getValueAt(current, 1).toString();
        Object webElement = table.getModel().getValueAt(current, 5);
        //if it's an element, highlight it
        //the element column is only in the model, so use the model to retreive it
        if (!(element.contentEquals(Const.TITLE))) {
            bd.highlight((WebElement)webElement, location);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String element = table.getModel().getValueAt(current, 0).toString();
        String location = table.getModel().getValueAt(current, 1).toString();
        String name = table.getModel().getValueAt(current, 2).toString();
        String id = table.getModel().getValueAt(current, 3).toString();
        Object value = table.getModel().getValueAt(current, 4);
        Object webElement = table.getModel().getValueAt(current, 5);
        //clicking on an element
        if (e.getActionCommand().contains(Const.CLICK)) {
            if (element.contentEquals("link") || element.contentEquals("image") || element.contentEquals("anchor") || element.endsWith(":radio") || element.endsWith(":checkbox") || element.endsWith(":submit") || element.endsWith(":button") || element.endsWith(":reset")) {
                //clickable
                bd.click((WebElement)webElement, location);
            } else if (element.endsWith(":select-one")) {
                //selection
                bd.select((WebElement)webElement, location);
            } else if (element.contentEquals(Const.TITLE)) {
                //page title
                bd.switchWin((String) webElement, (String)value);
            } else if (element.endsWith(":range") || element.endsWith(":color")) {
                //special input
                bd.inputjs((WebElement)webElement, location);
            } else {
                //general input element
                bd.input((WebElement)webElement, location);
            }
        }

        //finding an element
        if (e.getActionCommand()
                .contains(Const.FIND)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.find((WebElement)webElement, location, Const.FIND);
            }
        }

        //verifying the value of an element
        if (e.getActionCommand()
                .contains(Const.VERIFY)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
                if (location.contentEquals(Const.CURRENT)) {
                    //current page
                    bd.verifyPage((String)value, false);
                }
            } else {
                //page element
                bd.verify((WebElement)webElement, location, element, (String)value, false);
            }
        }

        //asserting the value of an element
        if (e.getActionCommand()
                .contains(Const.ASSERT)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
                if (location.contentEquals(Const.CURRENT)) {
                    //current page
                    bd.verifyPage((String)value, true);
                }
            } else {
                //page element
                bd.verify((WebElement)webElement, location, element, (String)value, true);
            }
        }

        //identifying an element
        if (e.getActionCommand()
                .contains(Const.IDENTIFY)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.ident((WebElement)webElement, location, (String) element, name, id, (String)value);

            }
        }
    }
}
