import {getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";
import {getCurrentLang, switchLang} from "../../../i18n";

export default {
  name: 'registration-submit-page',
  props: {
	submitToken: String,
	login: String
  },
  created: function () {
	if (!this.$route.params.login || !this.$route.params.submitToken) {
	  this.$router.push('/');
	  return;
	}

	this.form.login = this.$route.params.login;
	this.form.token = this.$route.params.submitToken;

	this.submit();
  },
  data: () => ({
	form: {
	  login: null,
	  token: null
	},
	sending: false,
	errors: null
  }),
  methods: {
	switchLang(lang) {
	  switchLang(lang);
	},
	getLangClass(lang) {
	  return getCurrentLang() === lang ? 'md-primary' : '';
	},
	submit() {
	  this.errors = [];
	  this.sending = true;

	  this.$http.post('auth/submitSignUp', {
		login: this.form.login,
		registration_token: this.form.token
	  }).then(
		response => {
		  let json = response.body;

		  if (!json.errors) {
			this.$router.push('/auth/signIn/' + this.form.login);
		  } else if (json.errors.length) {
			this.errors = this.errors.concat(json.errors.map((error) => getErrorMessage(error)));
		  } else {
			this.errors.push(getErrorMessage(UNEXPECTED));
		  }

		  this.sending = false;
		}, error => {
		  switch (error.status) {
			case 400:
			case 500:
			  let json = error.body;

			  if (json.errors) {
				this.errors = this.errors.concat(json.errors.map((error) => getErrorMessage(error)));
			  }
			  break;
		  }

		  if (this.errors.length <= 0) {
			this.errors.push('HTTP error (' + error.status + ': ' + error.statusText + ')');
		  }

		  this.sending = false;
		}
	  );
	}
  }
}
