import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScraperExecutionComponent } from './scraper-execution.component';

describe('ScraperExecutionComponent', () => {
  let component: ScraperExecutionComponent;
  let fixture: ComponentFixture<ScraperExecutionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ScraperExecutionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScraperExecutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
