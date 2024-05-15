import Vue from 'vue';
import {email, maxLength, minLength, required, sameAs} from "vuelidate/lib/validators/";
import {validationMixin} from "vuelidate";
import {
  LoginValidator, MAX_LOGIN_LENGTH, MAX_NAME_LENGTH, MAX_SURNAME_LENGTH, MIN_LOGIN_LENGTH, MIN_NAME_LENGTH,
  MIN_SURNAME_LENGTH, NameValidator
} from "../../../_validator/";
import {getLocalUser, resetLocalUser} from "../../../router/";
import {getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";
import {PasswordValidator} from "../../../_validator";
import {getCurrentLang, switchLang} from "../../../i18n";

export default {
  name: "SocialSuccessPage",
  mixins: [validationMixin],
  data: () => ({
	form: {
	  login: null,
	  password: null,
	  confirmPassword: null,
	  email: null,
	  name: null,
	  surname: null,
	},
	sending: false,
	errors: null,
	socialNetwork: null
  }),
  validations: {
	form: {
	  login: {
		required,
		minLength: minLength(MIN_LOGIN_LENGTH),
		maxLength: maxLength(MAX_LOGIN_LENGTH),
		LoginValidator
	  },
	  password: {
		required,
		minLength: minLength(8),
		PasswordValidator
	  },
	  confirmPassword: {
		required,
		sameAs: sameAs('password')
	  },
	  email: {
		required,
		email
	  },
	  name: {
		required,
		minLength: minLength(MIN_NAME_LENGTH),
		maxLength: maxLength(MAX_NAME_LENGTH),
		NameValidator
	  },
	  surname: {
		required,
		minLength: minLength(MIN_SURNAME_LENGTH),
		maxLength: maxLength(MAX_SURNAME_LENGTH),
		NameValidator
	  }
	}
  },
  created: function () {
	this.errors = [];
	this.sending = false;

	Vue.http.get('auth/getCurrentSession').then(
	  response => {
		let json = response.body;

		if (!json.errors) {
		  localStorage.setItem('user', JSON.stringify(json.data[0]));

		  this.$http.get('users/get/' + json.data[0].id)
			.then(
			  response => {
				let json = response.body;

				if (!json.data[0].login.match(/.*(facebook)|(google).*/)) {
				  Vue.router.push('/');
				}

				this.form.email = json.data[0].email;

				if (!json.data[0].name.match(/(Name)/)) {
				  this.form.name = json.data[0].name;
				}

				if (!json.data[0].surname.match(/(Surname)/)) {
				  this.form.surname = json.data[0].surname;
				}
			  });
		} else if (json.errors.length > 0) {
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
  },
  methods: {
	getValidationClass(fieldName) {
	  const field = this.$v.form[fieldName];

	  if (field) {
		return {
		  'md-invalid': field.$invalid && field.$dirty
		};
	  }
	},
	register() {
	  this.errors = [];
	  this.sending = true;

	  this.$http.post('auth/update',
		{
		  login: this.form.login,
		  password: this.form.password,
		  name: this.form.name,
		  surname: this.form.surname,
		  email: this.form.email
		}).then(
		response => {
		  let json = response.body;

		  if (!json.errors) {
			let user = getLocalUser();
			user.login = this.form.login;

			localStorage.setItem('user', JSON.stringify(user));
			this.$router.push('/');
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

		  if (!this.errors.length) {
			this.errors.push('HTTP error (' + error.status + ': ' + error.statusText + ')');
		  }

		  this.sending = false;
		}
	  );
	},
	validateCredentials() {
	  // this.$v.$touch();

	  // if (!this.$v.$invalid) {
	  this.register();
	  // }
	},
	cancel() {
	  this.$http.post('auth/logout').then(() => {
		resetLocalUser();
		this.$router.push('/');
	  });
	},
	switchLang(lang) {
	  switchLang(lang);
	},
	getLangClass(lang) {
	  return getCurrentLang() === lang ? 'md-primary' : '';
	}
  }
}
