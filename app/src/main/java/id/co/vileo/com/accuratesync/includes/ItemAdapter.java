package id.co.vileo.com.accuratesync.includes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.vileo.com.accuratesync.R;

/**
 * Created by suhe on 6/14/2016.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    TextView itemNo,itemName,categoryName,quantity,warningMessage;

    public ItemAdapter(Context context) {
        super(context, R.layout.item_rows);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rows, parent, false);
        }

        itemNo = (TextView) convertView.findViewById(R.id.item_no);
        itemName = (TextView) convertView.findViewById(R.id.item_name);
        categoryName = (TextView) convertView.findViewById(R.id.category_name);
        quantity = (TextView) convertView.findViewById(R.id.quantity);
        warningMessage = (TextView) convertView.findViewById(R.id.message_warning);
        Item item  = getItem(position);
        itemNo.setText(item.getItemNo());
        itemName.setText(item.getItemName());
        categoryName.setText(item.getCategoryName());
        quantity.setText(item.getQuantity());
        warningMessage.setText(item.getMessageWarning());
        return convertView;
    }

    public void swapRecords(List<Item> objects) {
        clear();
        for(Item object : objects) {
            add(object);
        }
        notifyDataSetChanged();
    }
}
