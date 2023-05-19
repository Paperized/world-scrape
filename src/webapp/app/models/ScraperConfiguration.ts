import {ScraperConfigurationParam} from "./ScraperConfigurationParam";

export class ScraperConfiguration {
  constructor(
    public id: number | null,
    public name: string,
    public urlStyle: string,
    public configurationUrl: string,
    public params: ScraperConfigurationParam[],
    public description?: string,
  ) {
  }
}
