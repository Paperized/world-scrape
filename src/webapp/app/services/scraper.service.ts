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

  runScraper(url: string, params: any) {
    params['url'] = url;
    return this.httpClient.post<any>(this.BASE_API + '/run', params);
  }

  getAllConfigurations() {
    return this.httpClient.get<ScraperConfiguration[]>(this.BASE_API + '/file-configs/all');
  }
}
