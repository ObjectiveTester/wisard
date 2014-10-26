package testingsteve;

/**
 *
 * @author Steve
 */
interface Const {
    
    //these are used internally to denote action and element type 
    
    static final String CLICK = "click";
    
    static final String FIND = "find";
            
    static final String VERIFY = "verify";
    
    static final String ASSERT = "assert";
    
    static final String IDENTIFY = "identify";
    
    static final String INVISIBLE = "(invisible)";
    
    static final String ID = "(id)";
    
    static final String PAGE = "page";
    
    static final String TITLE = "title";
    
    static final String WINDOW = "other window";
    
    static final String CURRENT = "current focus";
    
    //the seperator for nested frames
    
    static final String SEP = "->";
    
    //the style to 'highlight' an element
    
    static final String HIGHLIGHT = "'color: yellow; font-weight: bold; border: 2px dotted red;'";
    
    //max text to put in a dialog box
    static final int MAX_SIZ = 2048;
    
}
