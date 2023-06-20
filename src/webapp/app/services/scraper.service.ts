import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ScraperConfiguration} from "../models/ScraperConfiguration";
import {WS_BACKEND_URL, WS_SCRAPAPER_URL} from "../../environments/environment";
import {RunScraperDataReq, RunScraperDataRes} from "../models/RunScraperDataReq";

@Injectable({
  providedIn: 'root'
})
export class ScraperService {
  BASE_BE_API = WS_BACKEND_URL + '/scraper';
  BASE_SP_API = WS_SCRAPAPER_URL;
  constructor(private httpClient: HttpClient) { }

  runScraper(scraperData: RunScraperDataReq) {
    return this.httpClient.post<RunScraperDataRes>(this.BASE_SP_API + '/scrape', scraperData);
  }

  saveOrUpdateConfiguration(configuration: ScraperConfiguration, configText: string) {
    return this.httpClient.post<ScraperConfiguration>(this.BASE_BE_API + '/file-configs/create-or-update', {configuration, configText});
  }

  getAllConfigurations() {
    return this.httpClient.get<ScraperConfiguration[]>(this.BASE_BE_API + '/file-configs/all');
  }

  getConfigurationText(configuration: ScraperConfiguration) {
    return this.httpClient.get(configuration.configurationUrl!, {responseType: "text"});
  }

  deleteConfiguration(id: number) {
    return this.httpClient.delete(this.BASE_BE_API + '/file-configs/' + id);
  }
}
