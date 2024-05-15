import {LOGIN_PATTERN} from './LoginValidator'

export const PATTERN = /(^$|^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$)/

export const LoginOrEmailValidator = (value, component) => {
  return LOGIN_PATTERN.test(value) || PATTERN.test(value)
}
