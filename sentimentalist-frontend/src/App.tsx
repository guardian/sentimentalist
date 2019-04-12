import React from 'react';
import './App.css';
import {StreamGraph} from "./StreamGraph";
import {DataPoint} from "./shared";
import {Weighting, WeightingValue} from "./Weighting";
import {generateFakeData, TYPES} from "./fakeData";
import * as Chart from "chart.js";

export interface AppState {
  rawData: DataPoint[];
  data: DataPoint[];
  weightings: Map<string, WeightingValue>;
  hoverDay?: string;
  hoverDraw?: () => void;
}

const compareDataPoints = (a: DataPoint, b: DataPoint) => a.time.getTime() - b.time.getTime();

const applyWeighting = (weightings: Map<string, WeightingValue>) => (dataPoint: DataPoint) => {
  const weightingValue = weightings.get(dataPoint.type);
  return weightingValue ? Object.assign({}, dataPoint, {
    value: weightingValue.on ? weightingValue.weight * dataPoint.value : 0
  }) : dataPoint;
};

export class App extends React.Component<{}, AppState> {

  state: AppState = {
    rawData: [],
    data: [],
    weightings: new Map(),
  };

  componentDidMount(): void {
    const rawData: DataPoint[] = generateFakeData(999);
    this.setState({
      rawData,
      weightings: new Map(TYPES.map(type => [type, {on: true, weight: 1}]))
    }, this.refineData);


    const app = this;
    Chart.pluginService.register({
      afterDraw() {
        if(app.state.hoverDraw) app.state.hoverDraw();
      }
    });
  }


  private refineData(): void {

    const data = this.state.rawData
    .sort(compareDataPoints)
    .map(applyWeighting(this.state.weightings));

    this.setState({
      data
    });
  }

  render(): React.ReactNode {
    return (
      <>
        <div style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
          fontFamily: "\"Guardian Text Sans Web\", \"Helvetica Neue\", Helvetica, Arial, \"Lucida Grande\", sans-serif"
        }}>
          <div style={{float: "left"}}>
            <img src="logo.png" style={{width: "300px", display: "block"}}/>
            <img src="sub_logo.png" style={{width: "300px", display: "block"}}/>
          </div>
          <div id="filters" style={{
            padding: "1px 20px 20px",
            marginTop: "20px",
            textAlign: "left"
          }}>
            {Array.from(this.state.weightings.entries()).map(([type, weightingValue]) => (
              <Weighting
                key={type}
                type={type}
                currentWeighting={weightingValue}
                changeHandler={(newWeighting: WeightingValue) =>
                  this.setState(
                    {
                      weightings: this.state.weightings.set(type, newWeighting)
                    },
                    this.refineData
                  )
                }
                count={this.state.rawData.filter(dp => dp.type === type).length}
              />
            ))}
          </div>
          <div style={{
            textAlign: "right",
            margin: "20px"
          }}>
            <img src="positive.png" style={{width: "100px"}}/>
            <img src="neutral.png" style={{width: "100px"}}/>
            <br/>
            <img src="negative.png" style={{width: "100px"}}/>
          </div>
        </div>
        <div style={{
          height: "calc(100vh - 250px)",
          display: "flex"
        }}>
          <StreamGraph
            data={this.state.data}
            maxY={Math.max(...this.state.rawData.map(dp => dp.value))*3}
            setHoverDay={(hoverDay: string, hoverDraw: () => void) => {
              if(this.state.hoverDay!==hoverDay) this.setState({hoverDay, hoverDraw});
            }}
          />
          <iframe
            width="330"
            frameBorder="0"
            src={this.state.hoverDay ? `https://dashboard.ophan.co.uk/top20?day=${this.state.hoverDay}` : ""}
          ></iframe>
        </div>
      </>
    );
  }
}
