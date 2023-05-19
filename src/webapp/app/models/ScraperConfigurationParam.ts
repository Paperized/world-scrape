export type ScraperConfigParamType = 'text' | 'number' | 'bool';

export class ScraperConfigurationParam {
  constructor(
    public name: string,
    public type: ScraperConfigParamType,
    public value: Object | null,
    public id: number | null,
    public fileConfigId: number | null
  ) {
  }
}
