import {validationMixin} from "vuelidate";
import {minLength, required, sameAs} from "vuelidate/lib/validators/index";
import {LoginOrEmailValidator, MIN_LOGIN_LENGTH} from "../../../_validator";
import {ACCESS_DENIED, getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";

export default {
  name: "password-recovery-form",
  props: {
    recoveryToken: String,
    login: String
  },
  created: function () {
    if (this.$route.params.login && this.$route.params.recoveryToken) {
      this.form.login = this.$route.params.login;
      this.form.token = this.$route.params.recoveryToken;
      this.haveToken = true;
      this.fromEmailLink = true;
    }
  },
  mixins: [validationMixin],
  data: () => ({
    form: {
      login: null,
      token: null,
      password: null,
      confirmPassword: null
    },
    sending: false,
    errors: null,
    haveToken: false,
    showSnackBar: false,
    fromEmailLink: false
  }),
  validations: {
    form: {
      login: {
        required,
        minLength: minLength(MIN_LOGIN_LENGTH),
        LoginOrEmailValidator
      },
      token: {
        required
      },
      password: {
        required,
        minLength: minLength(8)
      },
      confirmPassword: {
        required,
        sameAs: sameAs('password')
      }
    }
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
    requestToken() {
      this.errors = [];
      this.sending = true;

      this.$http.post('auth/requestRecoveryToken', null,
        {
          params: {
            login: this.form.login
          }
        }).then(
        response => {
          let json = response.body;

          if (!json.errors) {
            this.haveToken = true;
          } else if (json.errors.length) {
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
    recoverPassword() {
      this.errors = [];
      this.sending = true;

      this.$http.post('auth/recoverPassword', {
        login: this.form.login,
        token: this.form.token,
        password: this.form.password
      }).then(
        response => {
          let json = response.body;

          if (!json.errors) {
            this.haveToken = false;
            this.showSnackBar = true;

            this.$parent.$parent.$parent.$parent.setLogin(json.data[0].login);
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
    snackBarAction() {
      this.$router.push('login');

      this.showSnackBar = false;
    },
    validateCredentials() {
      this.$v.$touch();

      if (this.haveToken) {
        if (!this.$v.$invalid) {
          this.recoverPassword();
        }
      } else if (!this.$v.form['login'].$invalid) {
        this.requestToken();
      }
    }
  }
}
