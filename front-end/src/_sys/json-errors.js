export const NO_ERROR = 0
export const UNEXPECTED = 1
export const ACCESS_DENIED = 2
export const IMAGE_ALREADY_EXISTS = 3
export const MAP_MARKER_ALREADY_EXISTS = 4
export const USER_ALREADY_EXISTS = 5
export const MISSING_FIELD = 6
export const BAD_FIELD_FORMAT = 7
export const USER_NOT_EXIST = 8
export const BAD_CREDENTIALS = 9
export const RECOVERY_TOKEN_EXPIRED = 10
export const IMAGE_NOT_EXIST = 11
export const MAP_MARKER_NOT_EXIST = 12
export const RECOVERY_TOKEN_NOT_EXIST = 13
export const ILLEGAL_PARAMETER = 14
export const TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS = 15
export const USER_BLOCKED_BY_MAX_FAILED_AUTH = 16

// eslint-disable-next-line
export function getErrorMessage(error) {
  switch (error.errno) {
    case NO_ERROR:
      return 'Everything fine'
    case UNEXPECTED:
      return 'Unexpected error'
    case ACCESS_DENIED:
      return 'Access denied'
    case IMAGE_ALREADY_EXISTS:
      return 'Image already exists'
    case MAP_MARKER_ALREADY_EXISTS:
      return 'Map marker already exists'
    case USER_ALREADY_EXISTS:
      return 'User with the same ' + error.field + ' already exists'
    case MISSING_FIELD:
      return 'The field ' + error.field + ' is required'
    case BAD_FIELD_FORMAT:
      return 'The field ' + error.field + ' has bad format'
    case USER_NOT_EXIST:
      return 'User with current ' + error.field + ' doesn\'t exist'
    case BAD_CREDENTIALS:
      return 'Password is wrong, it\'s your ' + error.field.substr(0, error.field.indexOf('/')) + ' failed attempt of ' + error.field.substr(error.field.indexOf('/') + 1, error.field.length)
    case RECOVERY_TOKEN_EXPIRED:
      return 'The recovery token is expired'
    case IMAGE_NOT_EXIST:
      return 'Image doesn\'t exist'
    case MAP_MARKER_NOT_EXIST:
      return 'Map marker doesn\'t exist'
    case RECOVERY_TOKEN_NOT_EXIST:
      return 'Recovery token doesn\'t exist'
    case ILLEGAL_PARAMETER:
      return 'The retrieved parameter is illegal'
    case TOO_MANY_NON_EXPIRED_RECOVERY_TOKENS:
      return 'There are too many non expired recovery tokens for the user'
    case USER_BLOCKED_BY_MAX_FAILED_AUTH:
      return 'The user has been blocked because of failed authentication attempts. Please, recover your password'
    default:
      return error.errmsg
  }
}
