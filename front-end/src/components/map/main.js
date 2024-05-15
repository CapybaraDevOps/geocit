import {maxLength, minLength, required} from 'vuelidate/lib/validators'
import {getLocalUser} from "../../router/index"


export default {
  name: 'Map',
  data: ()=> ({
    form: {
      title: null,
      desc: null,
      type: null,
      image: null
    },
    types: [],
    id: 0,
    lat: 0,
    lng: 0,
    marker: null,
    select: null,
    isPlaced: false,
    sending: false,
    showDialog: false,
    showSnackBar: false,
    showIssueDialog: true,
    searchParam: 'establishment',
    markers: [],
    issues: [],
    lats: [],
    lngs: [],
    activeMarker: {
        id: 0,
        lat: 0,
        lng: 0
      }
  }),
  validations: {
    form: {
      title: {
        required,
        minLength: minLength(4),
        maxLength: maxLength(32),

      },
      desc: {
        required,
        minLength: minLength(8),
        maxLength: maxLength(2048)
      },
      type: {
        required
      },
      image: {
        required
      }
    }
  },
  methods: {
    getValidationClass(fieldName) {
      const field = this.$v.form[fieldName];

      if (field) {
        return {'md-invalid': field.$invalid && field.$dirty}
      }
    },
    validateData() {
      this.$v.$touch();

      if (!this.$v.$invalid) {
        let fileSize = document.getElementById("uploadImage").files[0].size;

        if (fileSize > 1024 * 1024 * 5) {
          window.alert(this.$t('message.image_size'));
        } else {
          this.saveIssue();
        }
      }
    },
    clearForm() {
      this.$v.$reset();

      this.form.title = null;
      this.form.desc = null;
      this.form.type = null;
      this.form.image = null;
      document.getElementById("uploadImage").value = null
    },
    initMap() {
      let self = this;

      this.map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 48.29149, lng: 25.94034},
        zoom: 16,
        maxZoom: 20,
        minZoom: 14,
        disableDefaultUI: true,
        disableDoubleClickZoom: true,
        zoomControl: true,
        mapTypeControl: true,
      });

      this.addYourLocationButton();
      this.addSearchField();
      this.loadAllMarkers();

      if (localStorage.getItem('redirectFromIssue') && localStorage.getItem('activeMarker') !== null) {
        self.activeMarker = JSON.parse(localStorage.getItem('activeMarker'));

        self.showAllIssuesByMarker(self.activeMarker.id);

        let pos = {
          lat: self.activeMarker.lat,
          lng: self.activeMarker.lng
        };

        self.map.setCenter(pos);
        self.map.setZoom(20);
      } else {
        this.getUserLocation();
      }

      this.map.addListener('idle', function () {
        for (let i = 0; i < self.markers.length; i++) {
          let marker = self.markers[i];

          if (self.map.getBounds().contains(marker.getPosition()) && marker.getMap() !== self.map) {
            marker.setMap(self.map);
          }

          if (!self.map.getBounds().contains(marker.getPosition())) {
            marker.setMap(null);
          }
        }
      });

      this.map.addListener('click', function (e) {
        if (getLocalUser()) {
          e.stop();
          self.$http.get('auth/getCurrentSession')
            .then(response => {
              let json = response.body;

              if (json.data[0].logged_in) {
                self.clickHandler(e, self.map);
              } else {
                e.stop();

                self.showSnackBar = true
              }
            });
        } else {
          e.stop();

          self.showSnackBar = true
        }
      });
    },
    clickHandler(e, map) {
      let self = this;
      let k = 0;
      let placesService = new google.maps.places.PlacesService(map);

      if (e.placeId) {
        placesService.getDetails({placeId: e.placeId}, function (place) {
          for (let i = 0; i < self.lats.length; i++) {
            if (self.lats[i] === place.geometry.location.lat()
              && self.lngs[i] === place.geometry.location.lng()) {
              k++;
            }
          }

          if (k === 0) {
            self.saveCoords(place.geometry.location.lat(), place.geometry.location.lng());
          }
        });
        e.stop();
      } else {
        self.saveCoords(e.latLng.lat(), e.latLng.lng())
      }
    },
    searchHandler(lat, lng, s, place) {
      let self = this;
      let k = 0;
      for (let i = 0; i < self.lats.length; i++) {
        if (self.lats[i] === lat && self.lngs[i] === lng) {
          k++;
        }
      }

      if (k === 0) {
        self.map.setCenter(place.geometry.location);
        self.map.setZoom(20);

        s = new google.maps.Marker({
          map: self.map,
          position: {
            lat: place.geometry.location.lat(),
            lng: place.geometry.location.lng()
          },
          animation: google.maps.Animation.DROP
        });

        self.setMarkerType(s, '5');

        if (getLocalUser()) {
          self.$http.get('auth/getCurrentSession')
            .then(response => {
              let json = response.body;

              if (json.data[0].logged_in) {
                setTimeout(function () {
                  self.saveCoords(place.geometry.location.lat(), place.geometry.location.lng());
                  s.setMap(null);
                }, 1200);
              } else {
                self.showSnackBar = true;
              }
            });
        } else {
          self.showSnackBar = true;
          document.getElementById('pac-input').value = '';
        }
      } else {
        self.map.setCenter(place.geometry.location);
        self.map.setZoom(20);

        document.getElementById('pac-input').value = '';

        if (!getLocalUser()) {
          self.showSnackBar = true;
          document.getElementById('pac-input').value = '';
        }
      }
    },
    search() {
      let self = this;
      let pos;

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
        })
      } else {
        pos = new google.maps.LatLng(48.29149, 25.94034);
      }

      let autocomplete = new google.maps.places.Autocomplete(
        document.getElementById('pac-input'), {
          location: pos,
          radius: 10000,
        });

        autocomplete.setTypes([self.searchParam]);
        autocomplete.bindTo('bounds', self.map);
        autocomplete.addListener('place_changed', function() {
          let place = autocomplete.getPlace();
          let s = window.select;

         self.searchHandler(place.geometry.location.lat(), place.geometry.location.lng(), s, place);
      });
    },
    getUserLocation() {
      let self = this;
      navigator.geolocation.getCurrentPosition(function (position) {
        let pos = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };

        self.map.setCenter(pos);
        self.map.setZoom(20);

        let infoWindow = new google.maps.InfoWindow({map: self.map});

        infoWindow.setPosition(pos);
        infoWindow.setContent('<b>' + self.$t('message.user_location') + '</b>');
        setTimeout(function () {
          infoWindow.close();
        }, 2000)
      }, function () {
        self.handleLocationError()
      })
    },
    handleLocationError() {
      window.alert(this.$t('message.geolocation'));
    },
    addSearchField() {
      let self = this;
      let controlDiv = document.createElement('div');
      let input = document.createElement('input');

      input.setAttribute('placeholder', this.$t('label.input_location'));
      input.setAttribute('id', 'pac-input');
      input.setAttribute('type', 'text');

      input.style.marginTop = '10px';
      input.style.border = '1px solid transparent';
      input.style.borderRadius = '2px 0 0 2px';
      input.style.height = '29px';
      input.style.boxShadow = '0 1px 4px rgba(0, 0, 0, 0.3)';
      input.style.padding = '0 11px 0 13px';
      input.style.fontSize = '15px';
      input.style.borderColor = '#323232';

      input.addEventListener('click', function () {
        self.search()
      });

      let selectDiv = document.createElement('div');

      selectDiv.setAttribute('id', 'type-selector');

      selectDiv.style.color = '#fff';
      selectDiv.style.backgroundColor = '#323232';
      selectDiv.style.padding = '5px 11px 5px 11px';
      selectDiv.style.fontSize = '13px';

      let radio1 = document.createElement('input');

      radio1.setAttribute('type', 'radio');
      radio1.setAttribute('name', 'type');
      radio1.setAttribute('checked', 'checked');
      radio1.addEventListener('click', function () {
        self.searchParam = 'establishment';
      });

      let radio2 = document.createElement('input');

      radio2.setAttribute('type', 'radio');
      radio2.setAttribute('name', 'type');
      radio2.addEventListener('click', function () {
        self.searchParam = 'address';
      });

      let label1 = document.createElement('label');

      label1.setAttribute('for', 'radio1');
      label1.appendChild(document.createTextNode(this.$t('label.establishments')));

      let label2 = document.createElement('label2');

      label2.setAttribute('for', 'radio2');
      label2.appendChild(document.createTextNode(this.$t('label.addresses')));
      selectDiv.appendChild(radio1);
      selectDiv.appendChild(label1);
      selectDiv.appendChild(radio2);
      selectDiv.appendChild(label2);
      controlDiv.appendChild(input);
      controlDiv.appendChild(selectDiv);
      this.map.controls[google.maps.ControlPosition.TOP_LEFT].push(controlDiv);
    },
    addYourLocationButton() {
      let self = this;
      let controlDiv = document.createElement('div');
      let firstChild = document.createElement('button');
      firstChild.style.backgroundColor = '#fff';
      firstChild.style.border = 'none';
      firstChild.style.outline = 'none';
      firstChild.style.width = '28px';
      firstChild.style.height = '28px';
      firstChild.style.borderRadius = '2px';
      firstChild.style.boxShadow = '0 1px 4px rgba(0,0,0,0.3)';
      firstChild.style.cursor = 'pointer';
      firstChild.style.marginRight = '10px';
      firstChild.style.padding = '0px';
      firstChild.title = 'Find your location';

      controlDiv.appendChild(firstChild);

      let secondChild = document.createElement('div');
      secondChild.style.margin = '5px';
      secondChild.style.width = '18px';
      secondChild.style.height = '18px';
      secondChild.style.backgroundImage = 'url(https://maps.gstatic.com/tactile/mylocation/mylocation-sprite-1x.png)';
      secondChild.style.backgroundSize = '180px 18px';
      secondChild.style.backgroundPosition = '0px 0px';

      firstChild.appendChild(secondChild);
      firstChild.addEventListener('click', function () {
        self.getUserLocation()
      });
      this.map.controls[google.maps.ControlPosition.RIGHT_BOTTOM].push(controlDiv)
    },
    saveCoords(lat, lng) {
      window.lat = lat;
      window.lng = lng;

      this.openPopup()
    },
    openPopup() {
      let modal = document.getElementById('myModal');
      let span = document.getElementsByClassName("close")[0];
      modal.style.display = "table";
      span.onclick = function () {
        modal.style.display = "none";
        document.getElementById('pac-input').value = '';
        document.getElementById("preview").hidden = true;
        window.lat = 0;
        window.lng = 0;
      };
    },
    saveIssue() {
      let self = this;
      let title = this.form.title;
      let desc = this.form.desc;
      let type = this.form.type;
      let formData = new FormData();

      formData.append('file', document.getElementById("uploadImage").files[0]);

      if (window.isPlaced) {
        this.setMarkerType(window.marker, '4');
        this.map.setCenter(window.marker.getPosition());

        let infoWindow = new google.maps.InfoWindow({
          content: self.$t('message.new_issue')});
        infoWindow.open(self.map, window.marker);
        setTimeout(function () {
          infoWindow.close();
        }, 2000);

        // http://localhost:8080/citizen/map/issue?markerId=1&title=хуйхуйхуйх&desc=йцукенйцукен&typeName=INFO


        this.$http.post('map/issue', {
          markerId: window.id,
          title: title,
          desc: desc,
          typeName: type
        }).then((response) => {
          this.$http.put('issues/' + response.body.data[0].id, formData).then((response) => {
          });
        });
      } else {
        let marker = new google.maps.Marker({
            map: this.map,
            position: {
              lat: window.lat,
              lng: window.lng
            }
          });

          this.setMarkerType(marker, type);
          this.setListeners(marker);
          this.markers.push(marker);
          this.lats.push(marker.getPosition().lat());
          this.lngs.push(marker.getPosition().lng());
          this.map.setCenter(marker.getPosition());
          self.map.setZoom(20);

        let infoWindow = new google.maps.InfoWindow({
            content: self.$t('message.new_issue')});

        infoWindow.open(self.map, marker);
          setTimeout(function () {
            infoWindow.close();
          }, 2000);
          this.$http.post('map/marker', {
            lat: window.lat,
            lng: window.lng
          }).then((response) => {
            this.$http.post('map/issue', {
              markerId: response.body.data[0].id,
              title: title,
              desc: desc,
              typeName: type
            }).then((response) => {
                this.$http.put('issues/' + response.body.data[0].id, formData).then((response) => {
                });
              }
            );
          });
      }
      this.clearForm();

      window.marker = null;
      window.isPlaced = false;
      document.getElementById('pac-input').value = '';
      document.getElementById("preview").hidden = true;
      document.getElementById('myModal').style.display = "none";
    },
    setMarkerType(marker, type) {
      let url;

      switch(type) {
        case 'PROBLEM':
          url = './static/caution.png';
          break;
        case 'INFO':
          url = './static/info.png';
          break;
        case 'FEEDBACK':
          url = './static/feedback.png';
          break;
        case '4':
          url = './static/multiple.png';
          break;
        case '5':
          url = './static/select.png';
          break;
        default: url = ''
      }

      let icon = {
        url: url,
        scaledSize: new google.maps.Size(50, 50),
      };

      marker.setIcon(icon);
    },
    onCancelIssuesDialog() {
      this.issues = [];

      localStorage.removeItem('redirectFromIssue');
      localStorage.removeItem('activeMarker');
      localStorage.removeItem('zoom');
    },
    setListeners(marker) {
      let self = this;
      let timer = 0;
      let prevent = false;

      marker.addListener('click', function () {
          timer = setTimeout(function () {
            if (!prevent) {
              self.$http.get('map/marker/' + marker.getPosition().lat() + "/" + marker.getPosition().lng() + "/")
                .then((response) => {
                  self.activeMarker.id = response.body.data[0].id;
                  self.activeMarker.lat = parseFloat(response.body.data[0].lat);
                  self.activeMarker.lng = parseFloat(response.body.data[0].lng);

                  self.showAllIssuesByMarker(self.activeMarker.id);
                });
            }

            prevent = false;
          }, 300);

          marker.addListener('dblclick', function () {
            clearTimeout(timer);

            prevent = true;

            if (getLocalUser()) {
              self.$http.get('auth/getCurrentSession')
                .then(response => {
                  let json = response.body;

                  if (json.data[0].logged_in) {
                    self.getMarkerByCoords(marker.getPosition().lat(), marker.getPosition().lng());

                    window.marker = marker;
                    let modal = document.getElementById('myModal');
                    let span = document.getElementsByClassName("close")[0];
                    modal.style.display = "table";
                    span.onclick = function () {
                      modal.style.display = "none";
                    };
                  } else {
                    self.showSnackBar = true
                  }
                });
            } else {
              self.showSnackBar = true;
              document.getElementById('pac-input').value = '';
            }
          });
      })
    },
    showAllIssuesByMarker(markerId) {
      let self = this;

      self.$http.get("map/issues/mapMarker/" + markerId).then(response => {
        for (let i = 0; i < response.body.data.length; i++) {
          self.issues.push({
            id: response.body.data[i].id,
            title: response.body.data[i].title,
            description: response.body.data[i].text,
            typeId: response.body.data[i].type
          });
        }

        self.showDialog = true;
      })
    },
    getMarkerByCoords(lat, lng) {
      this.$http.get('map/marker/' + lat + "/" + lng + "/").then((response) => {
        window.id = response.body.data[0].id;
      });

      window.isPlaced = true;
    },
    previewImage() {
      let reader = new FileReader();

      reader.readAsDataURL(document.getElementById("uploadImage").files[0]);

      reader.onload = function (e) {
        document.getElementById("preview").src = e.target.result;
        document.getElementById("preview").hidden = false;
      };
    },
    snackBarAction() {
      this.$router.push('auth/login');
    },
    redirectToIssue(issueId, marker) {
      localStorage.setItem('activeMarker', JSON.stringify(marker));
      localStorage.setItem('zoom', this.map.getZoom());
      this.$router.push('issue/' + issueId);
    },
    loadAllMarkers() {
      let self = this;

      this.$http.get('map').then((response) => {
        for (let i = 0; i < response.body.data.length; i++) {
          let lat = parseFloat(response.body.data[i].lat);
          let lng = parseFloat(response.body.data[i].lng);
          let type = response.body.data[i].type.toString();
          let marker = new google.maps.Marker({
                position: {
                  lat: lat,
                  lng: lng
                }
              });

          this.setListeners(marker);

          if (type === '1') {
                self.setMarkerType(marker, 'PROBLEM');
          } else if (type === '2') {
                self.setMarkerType(marker, 'INFO');
          } else if (type === '3') {
                self.setMarkerType(marker, 'FEEDBACK');
          } else {
                self.setMarkerType(marker, '4');
              }
              this.lats.push(marker.getPosition().lat());
              this.lngs.push(marker.getPosition().lng());
              this.markers.push(marker);
          }

        for (let i = 0; i < this.markers.length; i++) {
          let marker = this.markers[i];

          if (self.map.getBounds().contains(marker.getPosition())) {
              marker.setMap(self.map);
            }
          }
        });
    },
  },
  mounted: function () {
      this.initMap();
  },
  created: function () {
    this.$http.get('issues/types').then(response => {
      let json = response.body;

      this.types = json.data;
    });
  },
  destroyed: function () {
    localStorage.removeItem('redirectFromIssue');
  }
}
