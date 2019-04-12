import {DataPoint, SentimentType} from "./shared";

export const TYPES = [
  "User Help",
  "CSR Case",
  "Cancellation Feedback",
  "Public Comments",
  "Letters to Editor"
];

const SENTIMENTS: SentimentType[] = [
  "POSITIVE",
  "NEUTRAL",
  "NEGATIVE"
];

export const NOW = new Date();

const SECONDS_IN_DAY = 60 * 60 * 24;

export const MILLIS_IN_DAY = SECONDS_IN_DAY * 1000;

const rand = (min: number, max: number) =>
  Math.floor(Math.random() * (max - min) ) + min;

export const generateFakeData: (n: number) => DataPoint[] = (n: number) =>
  new Array(n).fill(1).map(() => ({

    type: TYPES[rand(0, TYPES.length)],

    time: new Date(NOW.valueOf() - ((1000 * rand(0, SECONDS_IN_DAY)) + (MILLIS_IN_DAY * rand(0, 30)))),

    sentiment: SENTIMENTS[rand(0, SENTIMENTS.length)],

    value: rand(10, 20)

  }));