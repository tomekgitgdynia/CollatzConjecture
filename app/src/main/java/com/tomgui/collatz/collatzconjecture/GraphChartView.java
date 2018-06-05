package com.tomgui.collatz.collatzconjecture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom Kowszun on 6/1/2018.
 */
public class GraphChartView extends View {

    public static int BAR_GRAPH = 1;
    public static int LINE_GRAPH = 2;
    private RectF graphBounds;
    private Paint graphPaint;
    private List<ChartItem> chartItems = new ArrayList<ChartItem>();
    private int diagramType = 1;
    private static final String TAG = "GraphChartView";

    /**
     * Construct a GraphChartView
     *
     * @param context
     */
    public GraphChartView(Context context, List<ChartItem> chartItems) {
        super(context);
        this.chartItems = chartItems;
        graphPaint = new Paint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float oldTop = this.chartItems.get(0).top;
        float oldLeft = this.chartItems.get(0).left;
        for (ChartItem it : chartItems) {
            graphPaint.setColor(it.color);
            if(diagramType == 1)
            {
                canvas.drawRect(it.left, it.top, it.right, it.bottom, graphPaint);
            } else if(diagramType == 2)
            {
                canvas.drawLine(oldLeft, oldTop, it.left, it.top, graphPaint);
                oldTop = it.top;
                oldLeft = it.left;
            } else
            {
                canvas.drawPoint(it.left, it.top, graphPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        graphBounds = new RectF(0, 0, w, h);
    }

    public void updateData(List<ChartItem> chartItems)
    {
        this.chartItems = chartItems;
    }

    public void setDiagramType(int diagramType) {
        this.diagramType = diagramType;
    }


}