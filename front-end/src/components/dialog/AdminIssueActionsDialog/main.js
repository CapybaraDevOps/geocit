export default {
  name: "admin-issue-actions-dialog",
  data: () => ({
    showDialog: false,
    issue: null
  }),
  methods: {
    show(issue) {
      this.showDialog = true;
      this.issue = issue;
    },
    closeIssue(close) {
      this.$http.put('admin/issues/' + this.issue.id + '/close')
        .then(response => {
          let json = response.body;

          if (!json.errors) {
            this.$parent.issues[this.$parent.issues.indexOf(this.issue)].closed = close;
            this.$parent.searched = this.$parent.issues;
          } else if (json.errors.length) {
            // TODO: show error in snackBar
            console.log(JSON.stringify(json.errors));
          } else {
            // TODO: show Unexpected error in snackbar
            console.log('UNEXPECTED');
          }
        }, error => {
          console.log(JSON.stringify(error.body));
        });
    },
    hideIssue(hide) {
      this.$http.put('admin/issues/' + this.issue.id + '/hide')
        .then(response => {
          let json = response.body;

          if (!json.errors) {
            this.$parent.issues[this.$parent.issues.indexOf(this.issue)].hidden = hide;
            this.$parent.searched = this.$parent.issues;
          } else if (json.errors.length) {
            // TODO: show error in snackBar
            console.log(JSON.stringify(json.errors));
          } else {
            // TODO: show Unexpected error in snackbar
            console.log('UNEXPECTED');
          }
        }, error => {
          console.log(JSON.stringify(error.body));
        });
    },
    deleteIssue() {
      this.$http.delete('admin/issues/' + this.issue.id + '/')
        .then(response => {
          let json = response.body;

          if (!json.errors) {
            this.$parent.issues.splice(this.$parent.issues.indexOf(this.issue), 1);

            this.$parent.searched = this.$parent.issues;
            this.showDialog = false;
          } else if (json.errors.length) {
            // TODO: show error in snackBar
            console.log(JSON.stringify(json.errors));
          } else {
            // TODO: show Unexpected error in snackbar
            console.log('UNEXPECTED');
          }
        }, error => {
          console.log(JSON.stringify(error.body));
        });
    }
  }
}
