export type SentimentType = "POSITIVE" | "NEUTRAL"| "NEGATIVE";

export interface DataPoint {
  time: Date;
  sentiment: SentimentType;
  value: number;
  type: string;
}