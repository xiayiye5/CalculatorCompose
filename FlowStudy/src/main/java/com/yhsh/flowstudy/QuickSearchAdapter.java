package com.yhsh.flowstudy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuickSearchAdapter extends RecyclerView.Adapter<ContactsViewHolder> {
    private static final String TAG = "QuickSearchAdapter";
    private final String[][] contacts;

    public QuickSearchAdapter(String[][] contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactsItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
//        View contactsItem = View.inflate(parent.getContext(), R.layout.item_contact, null);
        return new ContactsViewHolder(contactsItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        if (position > 0) {
            //拿到上一个字母与当前字母比较，如果相同则隐藏itemNumberText
            String lastCase = contacts[position - 1][0];
            String currentCase = contacts[position][0];
            holder.itemNumberText.setVisibility(lastCase.equals(currentCase) ? View.GONE : View.VISIBLE);
        } else {
            holder.itemNumberText.setVisibility(View.VISIBLE);
        }
        holder.itemNumberText.setText(contacts[position][0]);
        holder.itemNameText.setText(contacts[position][1]);
    }

    @Override
    public int getItemCount() {
        return contacts.length;
    }


}

class ContactsViewHolder extends RecyclerView.ViewHolder {

    TextView itemNumberText;
    TextView itemNameText;

    public ContactsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemNumberText = itemView.findViewById(R.id.item_number_text);
        itemNameText = itemView.findViewById(R.id.item_name_text);
    }
}
