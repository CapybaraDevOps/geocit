import {getLocalUser} from "../../router";
import 'stompjs/lib/stomp.js';
import * as SockJS from 'sockjs-client/dist/sockjs.min.js'
import chatPage from '@/components/page/ChatPage/ChatPage.vue'
import {getCurrentLang, switchLang} from "../../i18n";
import {getServerAddress} from "../../main";

export default {
  name: 'AdminChatNotification',
  data: function () {
    return {
      users: [],
      stompClient: null
    }
  },
  components: {
    chatPage
  },
  methods: {
    answer: function (userId, issueId) {
      this.stompClient.send("/app/connect/accept", {},
        JSON.stringify({text: 'Accept', login: "", issueId: issueId, userId: userId, waiting: true}));
      window.location.href = "#/adminChatPage/" + issueId + "/" + userId;
    },
    markAsReaded: function (userId, issueId) {
      var _this = this;
      _this.stompClient.send('/app/connect/delete', {},
        JSON.stringify({text: 'Delete', login: '', issueId: issueId, userId: userId, waiting: false}));
      var index = -1;
      for (var i = 0; i < _this.users.length; i++) {
        if (_this.users[i].userId == userId && _this.users[i].issueId == issueId) {
          index = i;
          break;
        }
      }
      _this.users.splice(index, 1);
      _this.$parent.$parent.$parent.$parent.amount = _this.users.length;
    },
    switchLang(lang) {
      switchLang(lang);
    },
    getLangClass(lang) {
      return getCurrentLang() === lang ? 'md-primary' : '';
    }
  },
  created: function () {
    this.login = getLocalUser().login;
    let _this = this;

    var socket = new SockJS(getServerAddress() + "/chat");
    var stompClient = Stomp.over(socket);
    this.stompClient = stompClient;

    stompClient.connect({}, function (frame) {
      stompClient.subscribe('/checkTopic/broadcast', function (input) {
        var user = JSON.parse(input.body).data[0];
        if (user.text == "Accept" || user.text == "Cancel notification") {
          var index = -1;
          for (var i = 0; i < _this.users.length; i++) {
            if (_this.users[i].userId == user.userId && _this.users[i].issueId == user.issueId) {
              index = i;
              break;
            }
          }
          _this.users.splice(index, 1);
          _this.$parent.$parent.$parent.$parent.amount = _this.users.length;
        } else if (user.text == "Notification timed out") {
          var issueId = user.issueId;
          var userId = user.userId;
          var index = -1;
          for (var i = 0; i < _this.users.length; i++) {
            if (_this.users[i].userId == user.userId && _this.users[i].issueId == user.issueId) {
              index = i;
              break;
            }
          }
          _this.users[i].waiting = false;
        } else if (user.text == "Delete") {
          console.log('deleted');
        } else {
          var user = JSON.parse(input.body).data[0];
          console.log(user);
          _this.users.push(user);
          _this.$parent.$parent.$parent.$parent.amount = _this.users.length;
          console.log('received notification !');
        }
      });
    });

    this.$http.get('notification/all').then(data => {
      console.log(data.body.data);
      var charRequestArray = data.body.data;
      for (var i = 0; i < charRequestArray.length; i++) {
        var user = charRequestArray[i];
        this.users.push(user);
        this.$parent.$parent.$parent.$parent.amount = this.users.length;
      }
    });
    this.$parent.$parent.$parent.$parent.amount = this.users.length;
  }
}

