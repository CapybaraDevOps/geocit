import Vue from 'vue'
import VueResource from 'vue-resource'
import VueCookie from 'vue-cookie'
import VueI18n from 'vue-i18n'

import en from './lang/en.json'
import ru from './lang/ru.json'
import uk from './lang/uk.json'

Vue.use(VueI18n)
Vue.use(VueResource)
Vue.use(VueCookie)

const messages = {en, ru, uk}

// eslint-disable-next-line
export function getCurrentLang() {
  return Vue.cookie.get('lang')
}

const i18n = new VueI18n({
  locale: getCurrentLang() || 'en',
  messages
})

// eslint-disable-next-line
function loadLocaleMessage(locale, cb) {
  return fetch(`./lang/${locale}.json`, {
    method: 'get',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  }).then((res) => {
    return res.json()
  }).then((json) => {
    if (Object.keys(json).length === 0) {
      return Promise.reject(new Error('locale empty !!'))
    } else {
      return Promise.resolve(json)
    }
  }).then((message) => {
    cb(null, message)
  }).catch((error) => {
    cb(error)
  })
}

// eslint-disable-next-line
export function switchLang(lang) {
  Vue.http.get('users/currentLang?lang=' + lang)
    .then(() => {
      if (lang in i18n.messages) {
        i18n.locale = lang
      } else {
        loadLocaleMessage(lang, (err, message) => {
          if (err) {
            console.error(err)
            return
          }
          i18n.setLocaleMessage(lang, message)

          i18n.locale = lang
        })
      }
    })
}

export default i18n
