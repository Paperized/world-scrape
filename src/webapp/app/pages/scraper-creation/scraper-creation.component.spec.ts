import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScraperCreationComponent } from './scraper-creation.component';

describe('ScraperCreationComponent', () => {
  let component: ScraperCreationComponent;
  let fixture: ComponentFixture<ScraperCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScraperCreationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScraperCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
