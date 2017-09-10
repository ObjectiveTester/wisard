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
            popup.getComponent(0).setEnabled(true);   //click
            popup.getComponent(1).setEnabled(true);   //find
            popup.getComponent(2).setEnabled(true);   //assert
            popup.getComponent(3).setEnabled(true);   //identify

            int idx = table.rowAtPoint(e.getPoint());
            table.getSelectionModel().setSelectionInterval(idx, idx);
            current = table.getSelectedRow();

            //disable the invalid choices
            if ((table.getValueAt(current, 0).toString().contentEquals(Const.TITLE))) {
                //cant elementFind or ident a page, so grey it out
                popup.getComponent(1).setEnabled(false);
                popup.getComponent(3).setEnabled(false);
                if (!(table.getValueAt(current, 1).toString().contentEquals(Const.CURRENT))) {
                    //cant assert other pages
                    popup.getComponent(2).setEnabled(false);
                } else {
                    //cant click on current page
                    popup.getComponent(0).setEnabled(false);
                }
            }
            if (table.getValueAt(current, 0).toString().contains("form") && !table.getValueAt(current, 0).toString().contains(":")) {
                //can only assert form elements, not forms
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(2).setEnabled(false);
            }
            if (table.getValueAt(current, 0).toString().endsWith(":hidden")) {
                //cant click or id hidden form fields
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(3).setEnabled(false);
            }
            if (table.getValueAt(current, 0).toString().startsWith(Const.INVISIBLE)) {
                //cant click or id invisible objects
                popup.getComponent(0).setEnabled(false);
                popup.getComponent(3).setEnabled(false);
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
        String element = table.getModel().getValueAt(current, 0).toString();
        if (table.getModel().getValueAt(current, 1).getClass().equals(String.class)) {
            stack = (String) table.getModel().getValueAt(current, 1);
        } else {
            stack = (ArrayList) table.getModel().getValueAt(current, 1);
        }
        Object webElement = table.getModel().getValueAt(current, 5);
        //if it's an element, highlight it
        //the element column is only in the model, so use the model to retreive it
        if (!element.contentEquals(Const.TITLE) && !element.startsWith(Const.INVISIBLE)) {
            bd.highlight((WebElement) webElement, stack);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String element = table.getModel().getValueAt(current, 0).toString();
        Object stack;
        if (table.getModel().getValueAt(current, 1).getClass().equals(String.class)) {
            stack = (String) table.getModel().getValueAt(current, 1);
        } else {
            stack = (ArrayList) table.getModel().getValueAt(current, 1);
        }
        String name = table.getModel().getValueAt(current, 2).toString();
        String id = table.getModel().getValueAt(current, 3).toString();
        Object value = table.getModel().getValueAt(current, 4);
        Object webElement = table.getModel().getValueAt(current, 5);
        //clicking on an element
        if (e.getActionCommand().contains(Const.CLICK)) {
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
                .contains(Const.FIND)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.find((WebElement) webElement, stack, Const.FIND);
            }
        }

        //asserting the value of an element
        if (e.getActionCommand()
                .contains(Const.ASSERT)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
                if (stack.getClass().equals(ArrayList.class)) {
                    //current page
                    bd.verifyPage((String) value);
                }
            } else {
                //page element
                bd.verify((WebElement) webElement, stack, element, (String) value);
            }
        }

        //identifying an element
        if (e.getActionCommand()
                .contains(Const.IDENTIFY)) {
            if (element.contentEquals(Const.TITLE)) {
                //page title
            } else {
                //page element
                bd.ident((WebElement) webElement, stack, (String) element, name, id, (String) value);

            }
        }
    }
}
