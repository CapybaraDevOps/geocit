import {validationMixin} from 'vuelidate';
import {minLength, required} from 'vuelidate/lib/validators';
import {LoginOrEmailValidator, MIN_LOGIN_LENGTH} from "../../../_validator";
import {ACCESS_DENIED, getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";

export default {
  name: 'SignInPage',
  props: {
	login: String
  },
  mixins: [validationMixin],
  created: function () {
    if (this.$route.params.login) {
      this.form.login = this.$route.params.login;
    }
  },
  data: () => ({
    form: {
      login: null,
      password: null,
    },
    sending: false,
    errors: null
  }),
  validations: {
    form: {
      login: {
        required,
        minLength: minLength(MIN_LOGIN_LENGTH),
        LoginOrEmailValidator
      },
      password: {
        required,
        minLength: minLength(8)
      }
    }
  },
  methods: {
    setLogin(login) {
      this.form.login = login;
    },
    getValidationClass(fieldName) {
      const field = this.$v.form[fieldName];

      if (field) {
        return {
          'md-invalid': field.$invalid && field.$dirty
        };
      }
    },
    authorize() {
      this.errors = [];
      this.sending = true;

      this.$http.post('auth/login', null,
        {
          params: {
            login: this.form.login,
            password: this.form.password
          }
        }).then(
        response => {
          let json = response.body;

          if (!json.errors) {
            this.$router.push('/');
            localStorage.setItem('user', JSON.stringify(json.data[0]));
          } else if (json.errors.length > 0) {
            switch (json.errors[0].errno) {
              case ACCESS_DENIED:
                this.$router.push('/403');
                break;
              default:
                this.errors = this.errors.concat(json.errors.map((error) => getErrorMessage(error)));

                break;
            }
          } else {
            this.errors.push(getErrorMessage(UNEXPECTED));
          }

          this.sending = false;
        }, error => {
          switch (error.status) {
            case 403:
            case 404:
              this.$router.push('/' + error.status);
              break;
            default:
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
    validateCredentials() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        this.authorize();
      }
    }
  }
}
