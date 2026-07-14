import {Directive, ElementRef, EventEmitter, HostListener, inject, Output} from '@angular/core';

/**
 * <div (clickOutside)="closeMenu()"></div>
 */
@Directive({
  selector: '[clickOutside]',
  standalone: true
})
export class ClickOutsideDirective {
  @Output() clickOutside = new EventEmitter<void>();

  private readonly elementRef: ElementRef<HTMLElement> = inject(ElementRef);

  constructor() {}

  @HostListener('document:click', ['$event.target'])
  onClick(target: any) {
    if (!this.elementRef.nativeElement.contains(target)) {
      this.clickOutside.emit();
    }
  }
}
