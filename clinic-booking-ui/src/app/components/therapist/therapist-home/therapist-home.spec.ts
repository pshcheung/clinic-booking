import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TherapistHome } from './therapist-home';

describe('TherapistHome', () => {
  let component: TherapistHome;
  let fixture: ComponentFixture<TherapistHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TherapistHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TherapistHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
