package com.example.myshoppingapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ConfirmPurchaseDialog extends Dialog {
    public ConfirmPurchaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);

        Button no = findViewById(R.id.cancelButton);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void SetDetails(final int itemIndex,
                           final int itemId, final LinearLayout parentLayout)
    {
        Button yes = findViewById(R.id.purchaseButton);
        yes.setOnClickListener(new View.OnClickListener() {
            private int index = itemIndex;
            private int id = itemId;
            private LinearLayout layout = parentLayout;

            @Override
            public void onClick(View view) {
                for (int i = 0; i < layout.getChildCount(); ++i)
                {
                    View v = layout.getChildAt(i);
                    TextView hidden = v.findViewById(R.id.hiddenId);
                    int layoutID = Integer.parseInt("" + hidden.getText());
                    if (layoutID == id)
                    {
                        layout.removeViewAt(i);
                        break;
                    }
                }
                dismiss();
            }
        });

        TextView title = findViewById(R.id.nameText);
        title.setText(ShopDataManager.data.get(itemIndex).name);

        TextView desc = findViewById(R.id.description);
        desc.setText(ShopDataManager.data.get(itemIndex).description);

        TextView price = findViewById(R.id.cost);
        price.setText("$" + ShopDataManager.data.get(itemIndex).cost);

        ImageButton image = findViewById(R.id.itemImage);
        image.setImageResource(ShopDataManager.data.get(itemIndex).imageResource);
    }
}
