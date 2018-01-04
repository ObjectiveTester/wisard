package objectivetester;

/**
 *
 * @author Steve
 */
interface Const {

    //these are used internally to denote action and element type 
    static final String CLICK = "click";

    static final String FIND = "find";

    static final String ASSERT = "assert";

    static final String IDENTIFY = "identify";

    static final String INVISIBLE = "(invisible) ";

    static final String PAGE = "page";

    static final String TITLE = "title";

    static final String WINDOW = "other window";

    static final String CURRENT = "current focus";

    //the style to 'highlight' an element
    static final String HIGHLIGHT = "'color: yellow; font-weight: bold; border: 2px dotted red;'";

    //max text to put in a dialog box
    static final int MAX_SIZ = 2048;

    static final int POP_CLICK = 0;

    static final int POP_FIND = 1;

    static final int POP_ASSERT = 2;

    static final int POP_ID = 3;

    static final int TAB_ELEMENT = 0;

    static final int TAB_LOCATION = 1;

    static final int TAB_NAME = 2;

    static final int TAB_ID = 3;

    static final int TAB_VALUE = 3;

    static final int TAB_WEBELEMENT = 5;

}
