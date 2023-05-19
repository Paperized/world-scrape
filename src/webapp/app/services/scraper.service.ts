import { Injectable } from '@angular/core';
import {SERVER_API_URL} from "../constants";
import {HttpClient} from "@angular/common/http";
import {ScraperConfiguration} from "../models/ScraperConfiguration";

@Injectable({
  providedIn: 'root'
})
export class ScraperService {
  BASE_API = SERVER_API_URL + '/scraper';
  constructor(private httpClient: HttpClient) { }

  runScraper(url: string, config_url: string, params: any) {
    params['url'] = url;
    params['config_url'] = config_url;
    return this.httpClient.post<any>(this.BASE_API + '/run', params);
  }

  saveOrUpdateConfiguration(configuration: ScraperConfiguration, configText: string) {
    return this.httpClient.post<ScraperConfiguration>(this.BASE_API + '/file-configs/create-or-update', {configuration, configText});
  }

  getAllConfigurations() {
    return this.httpClient.get<ScraperConfiguration[]>(this.BASE_API + '/file-configs/all');
  }

  getConfigurationText(configuration: ScraperConfiguration) {
    return this.httpClient.get(configuration.configurationUrl!, {responseType: "text"});
  }

  deleteConfiguration(id: number) {
    return this.httpClient.delete(this.BASE_API + '/file-configs/' + id);
  }
}
