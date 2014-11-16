Game.factory('StoreItems', ['$rootScope', '$http', function($rootScope, $http) {

  return {
    load: function() {
      var res = $http.get("/storecontroller/storeitems");
      res.success(function(data) {
        $rootScope.storeItems = data;
      });
    }
  };
}]);

Game.factory('$farmer', ['$rootScope', '$http', '$items', '$location', 'State',
    function($rootScope, $http, $items, $location, State) {
      function swap(farmer) {
        if (farmer && farmer.hasOwnProperty('balans')) {
          State.set('farmer', farmer);
        }
      }
      function setStatus(status) {
    	  State.set('status',status);
    	  swap(status.farmer);
      }
      return {
        load: function() {
          /*
           * if ($rootScope.farmer) { return $rootScope.farmer; } else {
           */
          var res = $http.get("/AuthController/farmer");
          res.success(function(data) {
        	  setStatus(data);
        	  swap(data.farmer);
          });
          return res;
          // }
        },
        swap: swap,
        setStatus: setStatus
      };

    }]);

Game.factory('$day', ['$rootScope', 'State', '$http', '$weather', 'Diseases',
    '$items', '$location',
    function($rootScope, State, $http, $weather, Diseases, $items, $location) {

      function onFarmer(farmer) {
        if (!$rootScope.farmer && farmer.field) {
          $items.load();
        }
        if (farmer.balans != null) {
          $rootScope.day = farmer;
          $rootScope.farmer = farmer;
        }

        $location.path(farmer.currentState || '/buy_tractor');
      }

      State.subscribe('farmer', '$day', onFarmer);

      return {
        get: function() {
          return State.getByField('farmer');
        },
        load: function(farmer) {
          State.set('farmer', farmer);
        },
        next: function() {
          var res = $http.get("/application/nextday");
          res.success(function(data) {
            if (!$rootScope.farmer && data.farmer.field) {
              $items.load();
            }
            if (data.balans != null) {
              $rootScope.day = data;
              $rootScope.farmer = data;
            }

            $location.path(data.farmer.currentState || '/buy_tractor');

            $weather.load();
            Diseases.load();
          });
        },
        nextWeek: function() {
          var res = $http.get("/application/nextweek");
          res.success(function(data) {
            if (!$rootScope.farmer && data.field) {
              $items.load();
            }
            if (data.balans != null) {
              $rootScope.day = data;
              $rootScope.farmer = data;
            }

            $location.path(data.farmer.currentState || '/buy_tractor');

            $weather.load();
            Diseases.load();
          });
        },
        nextMonth: function() {
          var res = $http.get("/application/nextmonth");
          res.success(function(data) {
            if (!$rootScope.farmer && data.field) {
              $items.load();
            }
            if (data.balans != null) {
              $rootScope.day = data;
              $rootScope.farmer = data;
            }

            $location.path(data.farmer.	currentState || '/buy_tractor');

            $weather.load();
            Diseases.load();
          });
        }

      };
    }]);

Game.factory('$items', ['$rootScope', '$http', function($rootScope, $http) {
  if (!$rootScope.items) {
    $rootScope.items = {};
    $rootScope._items = [];
  }
  return {
    load: function(callback) {
      var res = $http.get("/storecontroller/myitems");
      res.success(function(data) {
        $rootScope.items = {};
        $rootScope._items = data || [];
        for (var i = 0; i < data.length; i++) {
          var store = data[i].store;
          $rootScope.items[store] = $rootScope.items[store] || [];
          $rootScope.items[store].push(data[i]);
        }
        if (callback && typeof callback === 'function') {
          callback();
        }
      });
    },
    add: function(key, val) {
      $rootScope.items[key] = $rootScope.items[key] || [];
      var vals = $rootScope.items[key];
      // todo: check for duplicates
      vals.push(val);
      $rootScope._items.push(val);
    },
    check: function(key) {
      return $rootScope.items[key] && $rootScope.items[key].length > 0;
    },
    get: function(key) {
      return $rootScope.items[key];
    },
    use: function(key, item) {
      $rootScope.items[key].pop();

      function remove(arr) {
        var results = [];
        var removed = false;
        angular.forEach(arr, function(val) {

          if (!removed && val.store == key) {
            if (!item || val.id == item.id) {
              removed = true;
            } else {
              results.push(val);
            }
          } else {
            results.push(val);
          }
        });
        return results;
      }
      $rootScope._items = remove($rootScope._items);
      $rootScope.items[key] = remove($rootScope.items[key]);
    },
    all: function() {
      var results = [];
      angular.forEach($rootScope.items, function(val) {
        angular.forEach(val, function(v) {
          results.push(v);
        });
      });
      return results;
    }

  };
}]);

Game.factory('$weather', ['$rootScope', '$http', function($rootScope, $http) {
  if (!$rootScope.weather) {
    $rootScope.weather = [];
  }
  return {
    load: function() {
      var res = $http.get("/WeatherController/weatherforecast?fordays=5");
      res.success(function(data) {
        $rootScope.weather = data;
      });
    }
  };
}]);

Game.factory('$plantation', [
    '$rootScope',
    '$http',
    function($rootScope, $http) {
      return {
        load: function(fn) {
          var res = $http.get("/AuthController/plantation");
          res.success(function(data) {
            $rootScope.plantation = data;
            if (fn && typeof fn === 'function') {
              fn(data);
            }
          });
        },
        save: function(array, seedlings, fn) {
          var res = $http.post("/PlantationController/savePlanting?array="
                  + array + '&seedlings=' + seedlings);
          res.success(function(data) {
            if (fn && typeof fn === 'function') {
              fn(data);
            }
          })
        }
      };
    }]);

Game.factory('Fertilize', ['State', '$http', function(State, $http) {

  return {
    fertilize: function(fertilizer, callback) {
      $http({
        method: 'POST',
        url: "/fertilizationcontroller/fertilize",
        data: $.param(fertilizer),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }).then(function(res) {
        if (res.data && res.data.status == true && res.data.farmer) {
          State.set('farmer', data.farmer);
          if (typeof callback === 'function') {
            callback();
          }
        }
      });
    }
  }
}]);
Game.factory('$spraying', ['$items', '$day', '$http', 'jQuery',
    function($items, $day, $http, $) {

      return {
        spray: function(item) {
          $http({
            method: 'POST',
            url: "/sprayingcontroller/spray",
            data: $.param({
              itemid: item.id
            }),
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          }).then(function(res) {
            if (res.data && res.data.balans) {
              $day.load(res.data);
            }
            $items.load();
          });
        }
      }
    }]);

Game.factory("$infoTable", ['$rootScope', '$http', function($rootScope, $http) {
  return {
    load: function(data) {
      if (data) {

      }
      $rootScope.infoTables = data;
    },
    getNews: function() {
      $http({
        url: '/infotablecontroller/news'
      }).then(function(res) {
        if (res && res.data && res.data.length > 0) {
          for (var i = 0; i < res.data.length; i++) {
            $rootScope.$emit('info-show', {
              infos: res.data,
              showNext: true,
              titleImageUrl: '/public/images/game/jabolko.png'
            });
          }
        }
      })
    }
  }
}]);

Game.factory("$soilAnalyse", ['$rootScope', '$http',
    function($rootScope, $http) {
      return {
        load: function(data) {
          $http({
            url: '/infotablecontroller/news'
          }).then(function(res) {
            if (res.data.length > 0) {
              $rootScope.soilAnalyse = data;
            }
          })
        }
      }
    }]);

// I provide a utility class for preloading image objects.
Game.factory("preloader", function($q, $rootScope) {

  // I manage the preloading of image objects. Accepts an array of image URLs.
  function Preloader(imageLocations) {

    // I am the image SRC values to preload.
    this.imageLocations = imageLocations;

    // As the images load, we'll need to keep track of the load/error
    // counts when announing the progress on the loading.
    this.imageCount = this.imageLocations.length;
    this.loadCount = 0;
    this.errorCount = 0;

    // I am the possible states that the preloader can be in.
    this.states = {
      PENDING: 1,
      LOADING: 2,
      RESOLVED: 3,
      REJECTED: 4
    };

    // I keep track of the current state of the preloader.
    this.state = this.states.PENDING;

    // When loading the images, a promise will be returned to indicate
    // when the loading has completed (and / or progressed).
    this.deferred = $q.defer();
    this.promise = this.deferred.promise;

  }

  // ---
  // STATIC METHODS.
  // ---

  // I reload the given images [Array] and return a promise. The promise
  // will be resolved with the array of image locations.
  Preloader.preloadImages = function(imageLocations) {

    var preloader = new Preloader(imageLocations);

    return (preloader.load());

  };

  // ---
  // INSTANCE METHODS.
  // ---

  Preloader.prototype = {

    // Best practice for "instnceof" operator.
    constructor: Preloader,

    // ---
    // PUBLIC METHODS.
    // ---

    // I determine if the preloader has started loading images yet.
    isInitiated: function isInitiated() {

      return (this.state !== this.states.PENDING);

    },

    // I determine if the preloader has failed to load all of the images.
    isRejected: function isRejected() {

      return (this.state === this.states.REJECTED);

    },

    // I determine if the preloader has successfully loaded all of the
    // images.
    isResolved: function isResolved() {

      return (this.state === this.states.RESOLVED);

    },

    // I initiate the preload of the images. Returns a promise.
    load: function load() {

      // If the images are already loading, return the existing promise.
      if (this.isInitiated()) {

      return (this.promise);

      }

      this.state = this.states.LOADING;

      for (var i = 0; i < this.imageCount; i++) {

        this.loadImageLocation(this.imageLocations[i]);

      }

      // Return the deferred promise for the load event.
      return (this.promise);

    },

    // ---
    // PRIVATE METHODS.
    // ---

    // I handle the load-failure of the given image location.
    handleImageError: function handleImageError(imageLocation) {

      this.errorCount++;

      // If the preload action has already failed, ignore further action.
      if (this.isRejected()) {

      return;

      }

      this.state = this.states.REJECTED;

      this.deferred.reject(imageLocation);

    },

    // I handle the load-success of the given image location.
    handleImageLoad: function handleImageLoad(imageLocation) {

      this.loadCount++;

      // If the preload action has already failed, ignore further action.
      if (this.isRejected()) {

      return;

      }

      // Notify the progress of the overall deferred. This is different
      // than Resolving the deferred - you can call notify many times
      // before the ultimate resolution (or rejection) of the deferred.
      this.deferred.notify({
        percent: Math.ceil(this.loadCount / this.imageCount * 100),
        imageLocation: imageLocation
      });

      // If all of the images have loaded, we can resolve the deferred
      // value that we returned to the calling context.
      if (this.loadCount === this.imageCount) {

        this.state = this.states.RESOLVED;

        this.deferred.resolve(this.imageLocations);

      }

    },

    // I load the given image location and then wire the load / error
    // events back into the preloader instance.
    // --
    // NOTE: The load/error events trigger a $digest.
    loadImageLocation: function loadImageLocation(imageLocation) {

      var preloader = this;

      // When it comes to creating the image object, it is critical that
      // we bind the event handlers BEFORE we actually set the image
      // source. Failure to do so will prevent the events from proper
      // triggering in some browsers.
      var image = $(new Image()).load(function(event) {

        try {
          // Since the load event is asynchronous, we have to
          // tell AngularJS that something changed.
          $rootScope.$apply(function() {

            preloader.handleImageLoad(event.target.src);

            // Clean up object reference to help with the
            // garbage collection in the closure.
            preloader = image = event = null;

          });
        } catch (err) {
          preloader.handleImageLoad(event.target.src);

          // Clean up object reference to help with the
          // garbage collection in the closure.
          preloader = image = event = null;
        }

      }).error(function(event) {

        // Since the load event is asynchronous, we have to
        // tell AngularJS that something changed.
        $rootScope.$apply(function() {

          preloader.handleImageError(event.target.src);

          // Clean up object reference to help with the
          // garbage collection in the closure.
          preloader = image = event = null;

        });

      }).prop("src", imageLocation);

    }

  };

  // Return the factory instance.
  return (Preloader);

});