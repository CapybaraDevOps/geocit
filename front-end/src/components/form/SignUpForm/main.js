import {email, maxLength, minLength, required, sameAs} from "vuelidate/lib/validators/index";
import {validationMixin} from "vuelidate";
import {
  LoginValidator, MAX_LOGIN_LENGTH, MAX_NAME_LENGTH, MAX_SURNAME_LENGTH, MIN_LOGIN_LENGTH, MIN_NAME_LENGTH,
  MIN_SURNAME_LENGTH, NameValidator, PasswordValidator
} from "../../../_validator/index";
import {ACCESS_DENIED, getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";

export default {
  name: "SignUpPage",
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
    showSnackBar: false
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

      this.$http.post('auth/signUp',
        {
          login: this.form.login,
          email: this.form.email,
          password: this.form.password,
          name: this.form.name,
          surname: this.form.surname
        }).then(
        response => {
          let json = response.body;

          if (!json.errors) {
            this.showSnackBar = true;

            this.$parent.$parent.$parent.$parent.setLogin(json.data[0].login);
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

          if (!this.errors.length) {
            this.errors.push('HTTP error (' + error.status + ': ' + error.statusText + ')');
          }

          this.sending = false;
        }
      )
    },
    snackBarAction() {
      this.$router.push('/auth/signIn');

      this.showSnackBar = false;
    },
    validateCredentials() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        this.register();
      }
    }
  }
}
