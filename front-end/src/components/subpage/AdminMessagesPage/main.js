import Vue from 'vue';
import {getLocalUser} from "../../../router";

export default {
  name: "AdminMessagesPage",
  data: () => ({
	chatRooms: [],
	searchString: null
  }),
  created: function() {
	Vue.http.get('chat/room/all/' + getLocalUser().id)
	  .then(response => {
		let json = response.body.data;

		if (!json.errors) {
		  console.log(json);
		  this.chatRooms = json;
		} else if (json.errors.length) {
		  // TODO: show error in snackBar
		  console.log(JSON.stringify(json.errors));
		} else {
		  // TODO: show Unexpected error in snackbar
		  console.log('UNEXPECTED');
		}
	  }, error => {
		// TODO: implement this shit, pls
		console.log(JSON.stringify(error.body));
	  });
  },
  methods: {
	onSelect(chatRoom) {
	  if (chatRoom) {
		this.$router.push('/adminChatPage/' + chatRoom.issueId + '/' + chatRoom.userId);
	  }
	},
	searchOnTable() {
	  // this.searched = search(this.users, this.searchString);
	}
  }
}
