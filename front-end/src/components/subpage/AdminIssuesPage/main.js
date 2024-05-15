import Vue from 'vue';

const toLower = text => {
  return text.toString().toLowerCase()
};

var debounce = require('debounce');

export default {
  name: "AdminIssuesPage",
  data: () => ({
    page: 0,
    size: 10,
    sort: 'title,asc',
    totalPages: null,
    searchString: null,
    selected: null,
    searched: [],
    issues: [],
    issueType: 'ALL'
  }),
  props: ['user'],
  created: function() {
    if (this.$route.params.user) {
      this.searchString = this.$route.params.user;

      this.search();
    }
  },
  methods: {
    load(page) {
      Vue.http.get('admin/issues/', {params: {page: page, size: this.size, sort: this.sort}}).then(response => {
        let json = response.body;

        if (!json.errors) {
          this.issues = json.data;
          this.searched = this.issues;
          this.totalPages = json.count / this.size;
          this.totalPages = (this.totalPages - Math.floor(this.totalPages) ? (this.totalPages | 0) + 1 : this.totalPages | 0);
        } else if (json.errors.length) {
          //   TODO: show error in snackBar
          console.log(JSON.stringify(json.errors));
        } else {
          // TODO: show Unexpected error in snackbar
          console.log('UNEXPECTED');
        }
      }, error => {
        // TODO: implement this shit, pls
        console.log(JSON.stringify(error.body));
      })
    },
    onSelect(issue) {
      if (issue) {
        this.$refs.aIssueActionsDialog.show(issue);

        this.selected = issue;
      }
    },
    search: debounce(function () {
      if (!this.searchString) {
        this.load(this.page, this.size, this.sort);
        return;
      }
      this.$http.get('admin/issues/search/' + encodeURIComponent(this.searchString), {
        params: {
          page: this.page,
          size: 10,
          sort: null
        }
      }).then(response => {
        let json = response.body;

        if (!json.errors) {
          this.issues = json.data;
          this.searched = this.issues;
          this.totalPages = json.count / this.size;
          this.totalPages = (this.totalPages - Math.floor(this.totalPages) ? (this.totalPages | 0) + 1 : this.totalPages | 0);
        } else if (json.errors.length) {
          //   TODO: show error in snackBar
          console.log(JSON.stringify(json.errors));
        } else {
          // TODO: show Unexpected error in snackbar
          console.log('UNEXPECTED');
        }
      }, error => {
        // TODO: implement this shit, pls
        console.log(JSON.stringify(error.body));
      });
    }, 500),
    filterByType() {
      let self = this;

      setTimeout(function () {
        if (self.issueType === 'ALL') {
          self.load(self.page, self.size, self.sort);
        } else {
          self.$http.get('admin/issues/' + encodeURIComponent(self.issueType), {
            params: {
              page: self.page,
              size: 10,
              sort: null
            }
          }).then(response => {
            let json = response.body;

            if (!json.errors) {
              self.issues = json.data;
              self.searched = self.issues;
              self.totalPages = json.count / self.size;
              self.totalPages = (self.totalPages - Math.floor(self.totalPages) ? (self.totalPages | 0) + 1 : self.totalPages | 0);
            } else if (json.errors.length) {
              //   TODO: show error in snackBar
              console.log(JSON.stringify(json.errors));
            } else {
              // TODO: show Unexpected error in snackbar
              console.log('UNEXPECTED');
            }
          }, error => {
            // TODO: implement this shit, pls
            console.log(JSON.stringify(error.body));
          });
        }
      }, 10);
    }
  }
}

