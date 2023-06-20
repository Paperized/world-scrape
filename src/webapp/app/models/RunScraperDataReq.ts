
export type RunScraperParamType = string | number | boolean | null | undefined;

export class RunScraperDataReq {
  public constructor(
    public url: string,
    public config_url: string,
    public debug?: boolean,
    public metrics?: boolean,
    public proxy?: string,
    public params?: Map<string, RunScraperParamType>,
  ) {
  }
}

export class RunScraperDataRes {
  public constructor(
    public values: Map<string, any>,
    public errors: Map<string, string>
  ) {
  }
}
