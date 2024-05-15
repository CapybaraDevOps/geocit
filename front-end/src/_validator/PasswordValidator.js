export const PASSWORD_PATTERN = /^(?=.*\d)(?=.*[a-zа-яіїє])(?=.*[A-ZА-ЯІЇЄ])(?=.*[-`!@#$%^&*()_+="'<>,./|\\?]).*$/

export const PasswordValidator = (value, component) => {
  return PASSWORD_PATTERN.test(value)
}
