import React from "react";

export interface WeightingValue {
  on: boolean;
  weight: number;
}

export interface WeightingProps {
  type: string;
  currentWeighting: WeightingValue;
  changeHandler: (newWeighting: WeightingValue) => void,
  count: number
}

const checkedChange = (props: WeightingProps) => (event: React.ChangeEvent<HTMLInputElement>) =>
  props.changeHandler(Object.assign({}, props.currentWeighting, {on: event.target.checked}));

const sliderChange = (props: WeightingProps) => (event: React.ChangeEvent<HTMLInputElement>) =>
  props.changeHandler(Object.assign({}, props.currentWeighting, {weight: parseInt(event.target.value)/100.0}));

export const Weighting = (props: WeightingProps) => (
  <div style={{marginLeft: "10px", display: "flex"}}>

    <input
      type="range"
      min="0"
      max="100"
      step="5"
      value={props.currentWeighting.weight*100}
      onChange={sliderChange(props)} />
    <label>
      <input
        type="checkbox"
        onChange={checkedChange(props)}
        checked={!props.currentWeighting || props.currentWeighting.on}
      />
      {props.type}{" "}
      (<span style={{textDecoration: props.currentWeighting.on ? undefined : "line-through"}}>{props.count}</span>)
    </label>
  </div>
);
