---
name: angular-22-developer
description: Expert system instructions for writing modern Angular 22 code. Use this for component generation, reactive features, and testing.
version: 1.0.0
---

# Angular 22 Developer Core Conventions

You are an expert AI agent working on an Angular 22 codebase. Follow these explicit framework specifications strictly to avoid generating deprecated boilerplate code.

## 1. Code Generation Rules
- **Standalone by Default:** All components, directives, and pipes must be standalone. Do not use `standalone: true` inside decorators as it is the implicit default in modern versions. Never use NgModules.
- **Zoneless & OnPush:** Code must be designed for zoneless change detection using `ChangeDetectionStrategy.OnPush`.
- **Signals-First State:** Prioritize signals (`signal()`, `computed()`, `effect()`) over RxJS BehaviorSubjects for component-level local state management.
- **Modern Control Flow:** Use strict `@if`, `@else`, `@for`, and `@switch` syntax blocks. Never use structural directives like `*ngIf` or `*ngFor`.
- **Type Safety:** Enforce strict type checking. Always implement robust type-safe `@switch` statements with exhaustive type-checking guards. Use Signal Forms with type-level inline validations.
- **Error Boundaries:** Wrap experimental or risky third-party integrations inside `@boundary` error structures paired with `@catch` blocks.

## 2. Router & Forms Configurations
- **Component Input Binding:** Map route parameters directly to component input signals using the modern router strategy rather than fetching from snapshots.
- **Forms Handling:** Prefer Signal Forms (stable in v22). If fallback reactive forms are absolutely necessary, leverage `fb.nonNullable.group` for explicit structure.

## 3. Post-Generation Verification
- After creating or editing code, you must execute verification scripts via the workspace runner.
- **Build Verification:** Run `ng build` to ensure type checks and template expressions compile cleanly. Do not mark a task complete if a compilation error surfaces.
- **Testing Standard:** The test runner of choice is Vitest. Ensure newly created tests fit within the existing suite.
