package id.co.vileo.com.accuratesync.includes;

/**
 * Created by suhe on 6/14/2016.
 */
public class Item {
    private String itemNo;
    private String itemName;
    private String categoryName;
    private String quantity;
    private String messageWarning;

    public Item(String itemNo, String itemName,String categoryName,String quantity,String messageWarning) {
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.messageWarning = messageWarning;
    }

    public String getItemNo() {
        return itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMessageWarning() {
        return messageWarning;
    }
}
