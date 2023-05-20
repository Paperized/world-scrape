import {ScraperConfigurationParam} from "./ScraperConfigurationParam";

export type ScraperConfigurationPolicy = 'PUBLIC' | 'PRIVATE';

export class ScraperConfiguration {
  constructor(
    public id: number | null,
    public name: string,
    public urlStyle: string,
    public configurationUrl: string,
    public policy: ScraperConfigurationPolicy,
    public params: ScraperConfigurationParam[],
    public userId?: number,
    public userUsername?: string,
    public description?: string
  ) {
  }
}
