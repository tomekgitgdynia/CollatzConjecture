package com.tomgui.collatz.collatzconjecture;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom Kowszun on 6/1/2018.
 */
public class MainActivity extends Activity {

    public static long MaxValueDisplay = -1;
    public static long MaxValueReal = -1;
    public static int MaxDigitsAllowed = 6;
    public int defaultValue = 3;
    private TextView graphLabelValue;
    private EditText enterNumberEditText;
    private ToggleButton graphTypeToggle;
    private InputMethodManager imm;
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final GraphChart graphChart = (GraphChart) this.findViewById(R.id.Graph);
        String numberEnteredText;

        if (savedInstanceState != null && savedInstanceState.getString("numberEntered") != null) {
            numberEnteredText = savedInstanceState.getString("numberEntered");
        } else {
            numberEnteredText = String.valueOf(defaultValue);

        }

        graphTypeToggle = (ToggleButton) this.findViewById(R.id.graphTypeTgl);
        if (savedInstanceState != null) {
            graphTypeToggle.setChecked(savedInstanceState.getBoolean("graphTypeToggled"));
        } else {
            graphTypeToggle.setChecked(true);
        }

        graphLabelValue = (TextView) this.findViewById(R.id.graphLblValue);
        enterNumberEditText = (EditText) this.findViewById(R.id.enterNumber);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(enterNumberEditText.getApplicationWindowToken(), 0);

        graphLabelValue.setText(numberEnteredText);
        graphChart.newNumberEntered(Integer.valueOf(numberEnteredText), graphTypeToggle.isChecked());
        if (numberEnteredText != null && numberEnteredText.length() > 0 && numberEnteredText.length() <= MaxDigitsAllowed)

            enterNumberEditText.setText(numberEnteredText);

        enterNumberEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        if (textView != null && textView.getText() != null && textView.getText().toString() != null && textView.getText().toString().length() > 0 && textView.getText().toString().length() <= MaxDigitsAllowed) {
                            if (Integer.valueOf(textView.getText().toString()) > 2) {
                                graphChart.newNumberEntered(Integer.valueOf(textView.getText().toString()), MainActivity.this.graphTypeToggle.isChecked());
                                graphLabelValue.setText(textView.getText().toString());
                                enterNumberEditText.setText("");
                                imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });

        graphTypeToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                graphChart.changeGraphType(graphTypeToggle.isChecked());
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (enterNumberEditText != null && graphLabelValue.getText() != null && graphLabelValue.getText().toString() != null && graphLabelValue.getText().toString().length() > 0 && graphLabelValue.getText().toString().length() <= MaxDigitsAllowed) {
            outState.putString("numberEntered", graphLabelValue.getText().toString());
        }
        if (graphTypeToggle != null) {
            outState.putBoolean("graphTypeToggled", graphTypeToggle.isChecked());
        }
    }

    public static List<ChartItem> generateChainData(int startingNumber) {
        List<ChartItem> chartItems = new ArrayList<>();

        long currentNumber = startingNumber;
        int chainCnt = 1;
        MaxValueDisplay = 0;
        MaxValueReal = 0;
        while (currentNumber > 1) {

            if (currentNumber > MaxValueDisplay) {
                MaxValueDisplay = currentNumber;
            }
            Log.d(TAG, " chainCnt -- " + chainCnt + " -- Number is " + currentNumber);
            ChartItem item = new ChartItem();
            item.mValue = currentNumber;
            chartItems.add(item);
            if ((currentNumber % 2) == 0) // even
            {
                currentNumber = currentNumber / 2;
            } else // odd
            {
                currentNumber = 3 * currentNumber + 1;
            }
            if (currentNumber > MaxValueDisplay) {
                MaxValueDisplay = currentNumber;
            }
            chainCnt++;
        }
        if (currentNumber > MaxValueDisplay) {
            MaxValueDisplay = currentNumber;
        }
        MaxValueReal = MaxValueDisplay;
        long originalMaxValue = MaxValueDisplay;
        updateMax();
        Log.d(TAG, " chainCnt -- " + chainCnt + " Number is " + currentNumber + " \n chain len = " + chainCnt + " new Max is " + MaxValueDisplay + " original Max " + originalMaxValue);
        ChartItem item = new ChartItem();
        item.mValue = currentNumber;
        chartItems.add(item);

        return chartItems;
    }

    static void updateMax() {
        if (MaxValueDisplay < 10) {
            for (int i = 0; i < 1000000; i = i + 1) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        } else if (MaxValueDisplay < 100) {
            for (int i = 0; i < 1000000; i = i + 10) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        } else if (MaxValueDisplay < 1000) {
            for (int i = 0; i < 1000000; i = i + 100) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        } else if (MaxValueDisplay < 10000) {
            for (int i = 0; i < 1000000; i = i + 1000) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        } else if (MaxValueDisplay < 100000) {
            for (int i = 0; i < 1000000; i = i + 10000) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        } else if (MaxValueDisplay < 1000000) {
            for (int i = 0; i < 1000000; i = i + 100000) {
                if (MaxValueDisplay < i) {
                    MaxValueDisplay = i;
                    break;
                }
            }
        }
    }
}

