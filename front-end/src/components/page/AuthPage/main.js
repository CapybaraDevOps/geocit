import {getCurrentLang, switchLang} from "../../../i18n";

export default {
  name: "AuthPage",
  methods: {
	setLogin(login) {
	  this.$refs.signInForm.form.login = login;
	},
	switchLang(lang) {
	  switchLang(lang);
	},
	getLangClass(lang) {
	  return getCurrentLang() === lang ? 'md-primary' : '';
	},
	getPosition(string, subString, index) {
	  return string.split(subString, index).join(subString).length;
	},
	getAuthRoute() {
	  return this.$route.path.substring(
		this.getPosition(this.$route.path, '/', 2) + 1,
		this.getPosition(this.$route.path, '/', 3));
	}
  }
}
