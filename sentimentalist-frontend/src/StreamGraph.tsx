import React from "react";
import {DataPoint, SentimentType} from "./shared";
import {Line} from "react-chartjs-2";
import {MILLIS_IN_DAY, NOW} from "./fakeData";

interface StreamGraphProps {
  data: DataPoint[];
  maxY: number;
  setHoverDay: (hoverDay: string, callback: () => void) => void
}

const sentitmentTypeToColour = (sentimentType: SentimentType) => {
  switch (sentimentType) {
    case "POSITIVE": return "#ccebc5";
    case "NEUTRAL": return "#b3cde3";
    case "NEGATIVE": return "#fbb4ae";
  }
};

const convertToDataset = (data: DataPoint[], groupBy: SentimentType) => ({
  label: groupBy,
  fill: true,
  backgroundColor: sentitmentTypeToColour(groupBy),
  data: data.filter(dataPoint => dataPoint.sentiment === groupBy).map(dataPoint => (
    {
      t: dataPoint.time,
      y: dataPoint.value
    }
  ))
});

export const StreamGraph = (props: StreamGraphProps) => (
  <div id="viz" style={{
    height: "100%",
    flexGrow: 1
  }}>
    <Line data={{
      datasets: [
        convertToDataset(props.data, "NEGATIVE"),
        convertToDataset(props.data, "NEUTRAL"),
        convertToDataset(props.data, "POSITIVE")
      ]
    }}
          options={{
            responsive: true,
            maintainAspectRatio: false,
            // Can't just just `stacked: true` like the docs say
            scales: {
              yAxes: [{
                stacked: true,
                display: false,
                ticks: {
                  beginAtZero: true,
                  max: props.maxY
                }
              }],
              xAxes: [{
                type: 'time'
              }],
            },
            elements:{
              point: {
                radius: 0
              },
              line: {
                cubicInterpolationMode: "monotone"
              }
            },
            animation: {
              duration: 750,
            },
            tooltips: {
              enabled: false,
              // intersect: false,
              // mode: 'index',
              // custom(a: any): void {
              //   const dataPoint = a.dataPoints[0];
              //   if(dataPoint) {
              //     const date = new Date(dataPoint.label);
              //     //
              //   }
              // }
            },
            events: ["mousemove"],
            onHover(event: MouseEvent): any {

              const chart = this;

              if(chart.ctx && chart.canvas) {

                const ctx = chart.canvas.getContext('2d');
                const widthPerDay = chart.canvas.width / 60;
                const daysSinceStart = Math.floor(event.clientX/widthPerDay);
                const xDayStart = daysSinceStart * widthPerDay;
                const millisAgo = MILLIS_IN_DAY * (30 - daysSinceStart);
                const date = new Date(NOW.getTime() - millisAgo);

                props.setHoverDay(date.toISOString().substr(0, 10), () => {
                  if (ctx) {
                    const oldFillColor = ctx.fillStyle;
                    ctx.fillStyle = "gray";
                    ctx.globalAlpha = 0.2;
                    ctx.fillRect(
                      xDayStart,
                      chart.chartArea.top,
                      widthPerDay,
                      chart.chartArea.bottom - chart.chartArea.top
                    );
                    ctx.fillStyle = oldFillColor;
                    ctx.globalAlpha = 1.0;
                  }

                });
              }
            }

          }}
    />
  </div>
);