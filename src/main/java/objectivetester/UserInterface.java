package objectivetester;

/**
 *
 * @author Steve
 */
interface UserInterface {

    void addItem(String type, Object stack, String name, String id, String value, Object element, Boolean displayed);

    void rescan();

    void abort();
    
    void finished();

    void addCode(String fragment);

    void insertCode(String fragment, int above);

    boolean alertResponse(String title);

    String enterValue(String title);

    String enterSelection(String title, String choices[]);

    void elementIdent(String message);

    void errorMessage(String message);
    
    String getCSSselectors();

}
