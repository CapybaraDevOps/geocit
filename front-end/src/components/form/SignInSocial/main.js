import {getServerAddress} from "../../../main";

export default {
  name: 'SignInPage',
  methods: {
	getServerAddress() {
	  return getServerAddress();
	}
  }
}
