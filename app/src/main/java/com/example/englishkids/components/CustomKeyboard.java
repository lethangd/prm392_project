package com.example.englishkids.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.englishkids.R;

import java.util.HashMap;

public class CustomKeyboard extends LinearLayout {

    private HashMap<Character, Button> letterButtonsMap = new HashMap<>();
    private OnKeyboardListener listener;
    private Button deleteButton;

    public CustomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setupKeyboard(context);
    }

    public void setOnKeyboardListener(OnKeyboardListener listener) {
        this.listener = listener;
    }

    private void setupKeyboard(Context context) {
        String[] rows = {"qwertyuiop", "asdfghjkl", "zxcvbnm"};

        for (int i = 0; i < rows.length; i++) {
            GridLayout rowLayout = new GridLayout(context);
            rowLayout.setColumnCount(rows[i].length() + (i == 2 ? 1 : 0));

            if (i == 1) rowLayout.setPadding(40, 0, 40, 0);
            else if (i == 2) rowLayout.setPadding(80, 0, 80, 0);

            for (char letter : rows[i].toCharArray()) {
                Button letterButton = new Button(context);
                letterButton.setText(String.valueOf(letter));
                letterButton.setTextSize(18);
                letterButton.setAllCaps(false);
                letterButton.setPadding(4, 4, 4, 4);

                // Set margins for the button
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(8, 8, 8, 8); // Set margins (left, top, right, bottom)
                letterButton.setLayoutParams(params);
                letterButton.setBackgroundResource(R.drawable.button_background);

                letterButton.setOnClickListener(view -> {
                    if (listener != null) listener.onLetterClick(letter);
                });

                letterButtonsMap.put(letter, letterButton);
                rowLayout.addView(letterButton);
            }

            if (i == 2) {
                deleteButton = new Button(context);
                deleteButton.setText("Xóa");
                deleteButton.setTextSize(10);
                deleteButton.setPadding(4, 4, 4, 4);
                deleteButton.setBackgroundResource(R.drawable.button_background);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
                params.setMargins(8, 8, 8, 8); // Set margins for the delete button
                deleteButton.setLayoutParams(params);

                deleteButton.setOnClickListener(view -> {
                    if (listener != null) listener.onDeleteClick();
                });
                rowLayout.addView(deleteButton);
            }

            addView(rowLayout);
        }
    }

    public void highlightButton(char letter) {
        if (letterButtonsMap.containsKey(letter)) {
            letterButtonsMap.get(letter).setBackgroundResource(R.drawable.button_highlight);
        }
    }

    public void highlightWrongLetter(char letter) {
        if (letterButtonsMap.containsKey(letter)) {
            letterButtonsMap.get(letter).setBackgroundResource(R.drawable.button_error);
        }
    }

    public void resetButtonHighlights() {
        for (Button button : letterButtonsMap.values()) {
            button.setBackgroundResource(R.drawable.button_background);
        }
    }

    public void disableButton(char letter) {
        if (letterButtonsMap.containsKey(letter)) {
            Button button = letterButtonsMap.get(letter);
            button.setEnabled(false);
            button.setAlpha(0.5f);
        }
    }

    public void enableButton(char letter) {
        if (letterButtonsMap.containsKey(letter)) {
            Button button = letterButtonsMap.get(letter);
            button.setEnabled(true);
            button.setAlpha(1.0f);
        }
    }

    public void enableAllButtons() {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            enableButton(letter); // Assuming enableButton(char letter) enables the specific button for that letter
        }
        enableDeleteButton();
    }
    public void disableDeleteButton() {
        if (deleteButton != null) {
            deleteButton.setEnabled(false);
            deleteButton.setAlpha(0.5f); // Giảm độ mờ để thể hiện nút đã bị vô hiệu hóa
        }
    }
    public void enableDeleteButton() {
        if (deleteButton != null) {
            deleteButton.setEnabled(true);
            deleteButton.setAlpha(1.0f);
        }
    }


    public interface OnKeyboardListener {
        void onLetterClick(char letter);
        void onDeleteClick();
    }
}
