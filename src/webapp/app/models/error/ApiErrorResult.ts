import {ApiError} from "./ApiError";

export class ApiErrorResult {
  constructor(
    public timestamp: string,
    public statusText: string,
    public errors: ApiError[]
  ) {
  }
}
