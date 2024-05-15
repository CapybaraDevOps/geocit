import {getLocalUser} from "../../../router";
import {validationMixin} from "vuelidate";
import {maxLength, minLength, required} from "vuelidate/lib/validators/index";
import {
  MAX_NAME_LENGTH, MAX_SURNAME_LENGTH, MIN_NAME_LENGTH, MIN_SURNAME_LENGTH,
  NameValidator
} from "../../../_validator";
import {ACCESS_DENIED, getErrorMessage, UNEXPECTED} from "../../../_sys/json-errors";
import {getServerAddress} from "../../../main";

export default {
  name: "UserPage",
  props: {
    id: Number
  },
  mixins: [validationMixin],
  data: () => ({
    user: null,
    showBack: true,
    editMode: false,
    sending: false,
    error: null,
    avatarHovered: false
  }),
  created: function () {
    this.$http.get('users/profile/' + this.$route.params.id)
      .then(response => {
        let json = response.body;

        if (!json.errors) {
          this.user = json.data[0];
          this.user.initials = this.user.name.charAt(0) + this.user.surname.charAt(0);
        }
      }, error => {
        this.$router.push('/' + error.status);
      });
  }, validations: {
    user: {
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
    edit() {
      if (!this.editMode) {
        this.editMode = true;

        return;
      }

      this.error = null;

      if (this.validateCredentials()) {
        this.sending = true;

        this.$http.put('users/edit', this.user).then(response => {
          let json = response.body;

          if (!json.errors) {
            this.editMode = false;
            this.user.name = json.data[0].name;
            this.user.surname = json.data[0].surname;
            this.user.initials = this.user.name.charAt(0) + this.user.surname.charAt(0);
          } else if (json.errors.length) {
            switch (json.errors[0].errno) {
              case ACCESS_DENIED:
                this.$router.push('/403');
                break;
              default:
                this.error = getErrorMessage(json.errors[0]);

                break;
            }
          } else {
            this.error = getErrorMessage(UNEXPECTED);
          }

          this.sending = false;
        }, error => {
          let json = error.body;

          switch (error.status) {
            case 500:
              this.error = getErrorMessage(json.errors[0]);

              break;
            case 403:
            case 404:
              this.$router.push('/' + error.status);
              break;
          }

          if (!this.error) {
            this.error = 'HTTP error (' + error.status + ': ' + error.statusText + ')';
          }

          this.sending = false;
        });
      }
    },
    updateImage(event) {
      this.sending = true;
      let formData = new FormData();

      formData.append('image', event.target.files[0]);
      this.$http.put('users/image', formData).then(response => {
        let json = response.body;

        if (!json.errors) {
          location.reload();
        } else if (json.errors.length) {
          switch (json.errors[0].errno) {
            case ACCESS_DENIED:
              this.$router.push('/403');
              break;
            default:
              this.error = getErrorMessage(json.errors[0]);

              break;
          }
        }

        this.sending = false;
      }, error => {
        let json = error.body;

        switch (error.status) {
          case 500:
            this.error = getErrorMessage(json.errors[0]);

            break;
          case 403:
          case 404:
            this.$router.push('/' + error.status);
            break;
        }

        if (!this.error) {
          this.error = 'HTTP error (' + error.status + ': ' + error.statusText + ')';
        }

        this.sending = false;
      });
    },
    validateCredentials() {
      this.$v.$touch();
      return !this.$v.$invalid;
    },
    getValidationClass(fieldName) {
      const field = this.$v.user[fieldName];

      if (field) {
        return {
          'md-invalid': field.$invalid && field.$dirty
        };
      }
    },
    getLocalUser() {
      return getLocalUser();
    },
    getServerAddress() {
      return getServerAddress();
    },
    fileInputClick() {
      document.getElementById('fileInput').click();
    }
  }
}
