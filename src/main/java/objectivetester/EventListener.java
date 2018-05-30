package objectivetester;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
            popup.getComponent(Const.POP_CLICK).setEnabled(true);   //click
            popup.getComponent(Const.POP_FIND).setEnabled(true);   //find
            popup.getComponent(Const.POP_ASSERT).setEnabled(true);   //assert
            popup.getComponent(Const.POP_ID).setEnabled(true);   //identify

            int idx = table.rowAtPoint(e.getPoint());
            table.getSelectionModel().setSelectionInterval(idx, idx);
            current = table.getSelectedRow();

            //disable the invalid choices
            if ((table.getValueAt(current, Const.TAB_ELEMENT).toString().contentEquals(Const.TITLE))) {
                //cant elementFind or ident a page, so grey it out
                popup.getComponent(Const.POP_FIND).setEnabled(false);
                popup.getComponent(Const.POP_ID).setEnabled(false);
                if (!(table.getValueAt(current, Const.TAB_LOCATION).toString().contentEquals(Const.CURRENT))) {
                    //cant assert other pages
                    popup.getComponent(Const.POP_ASSERT).setEnabled(false);
                } else {
                    //cant click on current page
                    popup.getComponent(Const.POP_CLICK).setEnabled(false);
                }
            }
            if (table.getValueAt(current, Const.TAB_ELEMENT).toString().contentEquals(Const.COOKIE)) {
                //can only assert and change a cookie
                popup.getComponent(Const.POP_CLICK).setEnabled(false);
                popup.getComponent(Const.POP_ID).setEnabled(false);
                popup.getComponent(Const.POP_FIND).setEnabled(false);
            }
            if (table.getValueAt(current, Const.TAB_ELEMENT).toString().contentEquals("form") && !table.getValueAt(current, Const.TAB_ELEMENT).toString().contains(":")) {
                //can only assert form elements, not forms
                popup.getComponent(Const.POP_CLICK).setEnabled(false);
                popup.getComponent(Const.POP_ASSERT).setEnabled(false);
            }
            if (table.getValueAt(current, Const.TAB_ELEMENT).toString().endsWith(":hidden")) {
                //cant click or id hidden form fields
                popup.getComponent(Const.POP_CLICK).setEnabled(false);
                popup.getComponent(Const.POP_ID).setEnabled(false);
            }
            if (table.getValueAt(current, Const.TAB_ELEMENT).toString().startsWith(Const.INVISIBLE)) {
                //cant click or id invisible objects
                popup.getComponent(Const.POP_CLICK).setEnabled(false);
                popup.getComponent(Const.POP_ID).setEnabled(false);
            }
            //show the popup menu
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void clickEvent(MouseEvent e) {
        int idx = table.rowAtPoint(e.getPoint());
        table.getSelectionModel().setSelectionInterval(idx, idx);
        current = table.getSelectedRow();
        Object stack;
        String element = table.getModel().getValueAt(current, Const.TAB_ELEMENT).toString();
        if (table.getModel().getValueAt(current, Const.TAB_LOCATION).getClass().equals(String.class)) {
            stack = (String) table.getModel().getValueAt(current, Const.TAB_LOCATION);
        } else {
            stack = (ArrayList) table.getModel().getValueAt(current, Const.TAB_LOCATION);
        }
        Object webElement = table.getModel().getValueAt(current, Const.TAB_WEBELEMENT);
        //if it's an element, highlight it
        //the element column is only in the model, so use the model to retreive it
        if (!element.contentEquals(Const.TITLE) && !element.startsWith(Const.INVISIBLE) && !element.contentEquals(Const.COOKIE)) {
            bd.highlight((WebElement) webElement, stack);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String element = table.getModel().getValueAt(current, Const.TAB_ELEMENT).toString();
        Object stack;
        if (table.getModel().getValueAt(current, Const.TAB_LOCATION).getClass().equals(String.class)) {
            stack = (String) table.getModel().getValueAt(current, Const.TAB_LOCATION);
        } else {
            stack = (ArrayList) table.getModel().getValueAt(current, Const.TAB_LOCATION);
        }
        String name = table.getModel().getValueAt(current, Const.TAB_NAME).toString();
        String id = table.getModel().getValueAt(current, Const.TAB_ID).toString();
        Object value = table.getModel().getValueAt(current, Const.TAB_VALUE);
        Object webElement = table.getModel().getValueAt(current, Const.TAB_WEBELEMENT);
        //clicking on an element
        if (e.getActionCommand().contentEquals(Const.CLICK)) {
            if (element.contentEquals("link") || element.contentEquals("image") || element.contentEquals("anchor") || element.endsWith(":radio") || element.endsWith(":checkbox") || element.endsWith(":submit") || element.endsWith(":button") || element.endsWith(":reset")) {
                //clickable
                bd.click((WebElement) webElement, stack);
            } else if (element.endsWith(":select-one")) {
                //selection
                bd.select((WebElement) webElement, stack);
            } else if (element.contentEquals(Const.TITLE)) {
                //page title
                bd.switchWin((String) webElement, (String) value);
            } else if (element.endsWith(":range") || element.endsWith(":color")) {
                //special input
                bd.inputjs((WebElement) webElement, stack);
            } else {
                //general input element
                bd.input((WebElement) webElement, stack);
            }
        }

        //finding an element
        if (e.getActionCommand()
                .contentEquals(Const.FIND)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.find((WebElement) webElement, stack, Const.FIND);
            }
        }

        //asserting the value of an element
        if (e.getActionCommand()
                .contentEquals(Const.ASSERT)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
                bd.verifyPage((String) value);
            } else if (element.contentEquals(Const.COOKIE)) {
                bd.verifyCookie((String) name, (String) value);
            } else {
                //page element
                bd.verify((WebElement) webElement, stack, element, (String) value);
            }
        }

        //identifying an element
        if (e.getActionCommand()
                .contentEquals(Const.IDENTIFY)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.ident((WebElement) webElement, stack, (String) element, name, id, (String) value);

            }
        }
    }
}
