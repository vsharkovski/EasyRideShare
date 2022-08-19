import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const passwordConfirmValidator: ValidatorFn = (
  control: AbstractControl
): ValidationErrors | null => {
  const password = control.get('password');
  const confirm = control.get('passwordConfirm');
  return password && confirm && password.value != confirm.value
    ? { passwordConfirm: true }
    : null;
};
