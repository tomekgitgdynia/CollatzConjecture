package com.tomgui.collatz.collatzconjecture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tom Kowszun on 6/1/2018.
 */
public class GraphChart extends ViewGroup {

    private List<ChartItem> graphItems = new ArrayList<ChartItem>();
    private RectF graphChartBounds = new RectF();
    private RectF componentBounds = new RectF();

    private Paint graphBackPaint;
    private Paint graphComponentBackPaint;
    private Paint boxPaint;
    private Paint textPaint;
    private int graphColor;

    private int currentNumber = 3;
    private GraphChartView graphChartView;
    private int minimumWidth = 100;
    private int yAxisOffset = 10;
    private int approximateCharacterWidth = 13;
    float leftMargin = 0;
    float rightMargin = 0;
    float bottomMargin = 0;
    float topMargin = 0;
    float barWidth = 0;
    float minMarginSize = 20;
    int numLeftMajorTicks = 5;
    int numLeftMinorTicks = 20;
    int numBottomTicks = 5;

    int tickWidth = 1;
    int ticLength = 8;
    int textRightOffset = 5;
    int textBottomOffset = 12;
    int lineOffset = 10;

    private static final String TAG = "GraphChart";


    /**
     * Custom component that shows
     * a chart graph.
     */
    public GraphChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GraphChart, 0, 0);
        try {

            leftMargin = attributes.getDimension(R.styleable.GraphChart_leftMargin, 0);
            topMargin = attributes.getDimension(R.styleable.GraphChart_topMargin, 0);
            rightMargin = attributes.getDimension(R.styleable.GraphChart_rightMargin, 0);
            bottomMargin = attributes.getDimension(R.styleable.GraphChart_bottomMargin, 0);
            barWidth = attributes.getDimension(R.styleable.GraphChart_barWidth, 0);

            graphComponentBackPaint = new Paint();
            graphComponentBackPaint.setColor(attributes.getColor(R.styleable.GraphChart_graphComponentBackColor, Color.WHITE));
            graphComponentBackPaint.setStyle(Paint.Style.FILL);

            graphColor = attributes.getColor(R.styleable.GraphChart_graphColor, Color.BLACK);

            graphBackPaint = new Paint();
            graphBackPaint.setColor(attributes.getColor(R.styleable.GraphChart_graphBackColor, Color.WHITE));
            graphBackPaint.setStyle(Paint.Style.FILL);

            boxPaint = new Paint();
            boxPaint.setColor(attributes.getColor(R.styleable.GraphChart_boxColor, Color.BLACK));
            boxPaint.setStyle(Paint.Style.STROKE);

            textPaint = new Paint();
            textPaint.setColor(attributes.getColor(R.styleable.GraphChart_textColor, Color.BLACK));
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(20);

        } finally {
            attributes.recycle();
        }

        init();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Do nothing. Do not call the superclass method--that would start a layout pass
        // on this view's children. GraphChart lays out its children in onSizeChanged().
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(componentBounds, graphBackPaint);

        // draw the bottom scale line and then tic marks with numbers
        canvas.drawLine(graphChartBounds.left - lineOffset, (graphChartBounds.bottom), graphChartBounds.right, (graphChartBounds.bottom), boxPaint);
        paintBottomNumbersAndTics(canvas, numBottomTicks);

        // draw the left side scale line and then minor tic marks and major tic marks with numbers
        canvas.drawLine(graphChartBounds.left - lineOffset, (graphChartBounds.bottom), graphChartBounds.left - lineOffset, (graphChartBounds.top), boxPaint);
        paintLeftTics(canvas, numLeftMinorTicks);
        paintLeftNumbersAndTics(canvas, numLeftMajorTicks);
    }

    private void paintBottomNumbersAndTics(Canvas canvas, int numOfTics) {
        if (numOfTics > graphItems.size()) {
            numOfTics = graphItems.size();
        }

        int numberOfDataPoints = graphItems.size();
        double ticStep = (graphChartBounds.width() - tickWidth) / (numberOfDataPoints - 1);
        int valueTicStep = numberOfDataPoints / numOfTics;
        int stepHolder = 0;
        for (int i = 0; i < numberOfDataPoints; i++) {
            stepHolder++;
            if (stepHolder > valueTicStep) {
                canvas.drawLine(graphChartBounds.left + (float) (ticStep * i), (graphChartBounds.bottom), (float) (graphChartBounds.left + ticStep * i), (graphChartBounds.bottom + ticLength), boxPaint);

                String valueTic = String.valueOf(Math.round((i) + 1));
                canvas.drawText(valueTic, (float) ((graphChartBounds.left + ticStep * i) + textRightOffset), (graphChartBounds.bottom + ticLength + textBottomOffset), textPaint);
                stepHolder = 0;
            }
        }
    }

    private void paintLeftTics(Canvas canvas, int numOfTics) {
        int ticCNT = 1;
        double ticStep = graphChartBounds.height() / (numOfTics);
        while (ticCNT <= numOfTics) {
            canvas.drawLine(graphChartBounds.left - yAxisOffset, (graphChartBounds.bottom - (float) (ticStep * ticCNT)), graphChartBounds.left + ticLength - yAxisOffset, (float) (graphChartBounds.bottom - ticStep * ticCNT), boxPaint);
            ticCNT++;
        }
    }

    private void paintLeftNumbersAndTics(Canvas canvas, int numOfTics) {
        int ticCNT = 1;
        double ticStep = graphChartBounds.height() / (numOfTics);
        double verticalValueStep = (MainActivity.MaxValueDisplay) / (numOfTics);

        while (ticCNT <= numOfTics) {
            canvas.drawLine(graphChartBounds.left - yAxisOffset, (graphChartBounds.bottom - (float) (ticStep * ticCNT)), graphChartBounds.left + ticLength - yAxisOffset, (float) (graphChartBounds.bottom - ticStep * ticCNT), boxPaint);
            String valueTic = String.valueOf(Math.round(ticCNT * verticalValueStep));
            canvas.drawText(valueTic, graphChartBounds.left - yAxisOffset - leftMargin, (float) (graphChartBounds.bottom - ticStep * ticCNT), textPaint);
            ticCNT++;
        }
    }

    //
    // Measurement functions. This example uses a simple heuristic: it assumes that
    // the graph chart should be at least as wide as its label.
    //
    @Override
    protected int getSuggestedMinimumWidth() {
        return minimumWidth * 2;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return minimumWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        int minh = (w - minimumWidth) + getPaddingBottom() + getPaddingTop();
        int h = Math.min(MeasureSpec.getSize(heightMeasureSpec), minh);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;
        componentBounds = new RectF(0.0f, 0.0f, ww, hh);
        componentBounds.offsetTo(getPaddingLeft(), getPaddingTop());

        leftMargin = String.valueOf(MainActivity.MaxValueDisplay).length() * approximateCharacterWidth;
        if (leftMargin < minMarginSize) leftMargin = minMarginSize;

        graphChartBounds = new RectF(componentBounds.left + leftMargin, componentBounds.top + topMargin, componentBounds.right - rightMargin, componentBounds.bottom - bottomMargin);

        // Lay out the child view that actually draws the graph.
        graphChartView.layout((int) graphChartBounds.left, (int) graphChartBounds.top, (int) graphChartBounds.right, (int) graphChartBounds.bottom);
        onDataChanged();
    }

    /**
     * Do all of the recalculations needed when the data array changes.
     */
    private void onDataChanged() {

        leftMargin = String.valueOf(MainActivity.MaxValueDisplay).length() * approximateCharacterWidth;
        if (leftMargin < minMarginSize)

            leftMargin = minMarginSize;

        graphChartBounds = new RectF(componentBounds.left + leftMargin, componentBounds.top + topMargin, componentBounds.right - rightMargin, componentBounds.bottom - bottomMargin);

        // Lay out the child view that actually draws the graph.
        graphChartView.layout((int) graphChartBounds.left, (int) graphChartBounds.top, (int) graphChartBounds.right, (int) graphChartBounds.bottom);

        int numberOfDataPoints = graphItems.size();
        double yOrigin = 0;
        int xOrigin = 0;

        double graphLineSingleStep = (graphChartBounds.width() - barWidth) / (numberOfDataPoints - 1);
        double graphValueSingleStep = (graphChartBounds.height()) / (MainActivity.MaxValueDisplay);
        Log.d(TAG, " graphChartBounds.width() " + graphChartBounds.width() + " graphChartBounds.height() " + graphChartBounds.height());
        int cnt = 0;
        for (ChartItem item : graphItems) {
            item.left = (float) (xOrigin + (graphLineSingleStep * cnt));
            item.right = item.left + barWidth;
            item.top = graphChartBounds.height() - (float) ((graphValueSingleStep * item.mValue));
            item.bottom = graphChartBounds.height();
            item.color = graphColor;
            cnt++;
            Log.d(TAG, "Item update " + " left " + item.left + " right " + item.right + " top " + item.top + " bottom " + item.bottom + " value " + item.mValue);
        }

    }


    public void init() {
        graphItems = MainActivity.generateChainData(currentNumber);
        graphChartView = new GraphChartView(getContext(), graphItems);
        addView(graphChartView);
    }

    public void newNumberEntered(int numberEntered, boolean graphTypeTgl) {
        this.currentNumber = numberEntered;
        this.graphItems.clear();
        this.graphItems = MainActivity.generateChainData(numberEntered);
        graphChartView.updateData(this.graphItems);
        onDataChanged();
        changeGraphType(graphTypeTgl);
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) this.getWidth() - xpad;
        float hh = (float) this.getHeight() - ypad;

        // Lay out the child view that actually draws the graph.
        graphChartView.layout((int) graphChartBounds.left, (int) graphChartBounds.top, (int) graphChartBounds.right, (int) graphChartBounds.bottom);
        this.invalidate();
        graphChartView.invalidate();

    }

    public void changeGraphType(boolean toggeld) {
        if (toggeld) {
            graphChartView.setDiagramType(GraphChartView.BAR_GRAPH);
        } else {
            graphChartView.setDiagramType(GraphChartView.LINE_GRAPH);
        }
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) this.getWidth() - xpad;
        float hh = (float) this.getHeight() - ypad;

        // Lay out the child view that actually draws the graph.
        graphChartView.layout((int) graphChartBounds.left, (int) graphChartBounds.top, (int) graphChartBounds.right, (int) graphChartBounds.bottom);
        this.invalidate();
        graphChartView.invalidate();

    }
}
