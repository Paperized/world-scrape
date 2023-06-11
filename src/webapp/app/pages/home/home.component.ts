import {Component, OnInit} from '@angular/core';
import {WS_BACKEND_URL} from "../../../environments/environment";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  ngOnInit(): void {

    console.log(WS_BACKEND_URL);
  }

}
