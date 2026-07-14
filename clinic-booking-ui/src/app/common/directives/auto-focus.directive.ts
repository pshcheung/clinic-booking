import { Directive, ElementRef, AfterViewInit } from '@angular/core';

/**
 * <input type="text" id="name" class="form-control" name="name" ngModel appAutofocus required>
 */
@Directive({
  selector: '[appAutofocus]'
})
export class AutofocusDirective implements AfterViewInit {
  // Inject ElementRef to get a reference to the host element
  constructor(private el: ElementRef) {}

  // AfterViewInit lifecycle hook ensures the element is rendered before focus is applied
  ngAfterViewInit() {
    // can add a setTimeout with a small delay (e.g., 0) if the element is part of a complex animated modal
    this.el.nativeElement.focus();
  }
}
