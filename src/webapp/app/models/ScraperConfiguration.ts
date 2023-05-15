import {ScraperConfigurationParam} from "./ScraperConfigurationParam";

export class ScraperConfiguration {
  constructor(
    public id: number,
    public name: string,
    public urlStyle: string,
    public params: ScraperConfigurationParam[],
    public description?: string,
  ) {
  }
}
