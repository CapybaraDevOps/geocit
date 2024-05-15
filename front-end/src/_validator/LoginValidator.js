export const MIN_LOGIN_LENGTH = 4
export const MAX_LOGIN_LENGTH = 16
export const LOGIN_PATTERN = /^[A-Za-z_\-.0-9]+$/

export const LoginValidator = (value, component) => {
  return LOGIN_PATTERN.test(value)
}
