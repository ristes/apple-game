Game.factory('$farmer', ['$rootScope', '$http', '$items', '$location',
    function($rootScope, $http, $items, $location) {
      function swap(farmer) {
        if (!$rootScope.farmer) {
          $items.load();
        }
        if ($rootScope.farmer === farmer) return;
        $rootScope.farmer = farmer;
        $location.path(farmer.currentState || '/buy_tractor');
      }
      return {
        load: function() {
          if ($rootScope.farmer) {
            return $rootScope.farmer;
          } else {
            // var res = $http.get("/public/javascripts/ng/mock/farmer.json");
            var res = $http.get("/AuthController/farmer");
            res.success(function(data) {
              swap(data);
            });
            return res;
          }
        },
        swap: swap
      };

    }]);

Game.factory('$items', ['$rootScope', '$http', function($rootScope, $http) {
  if (!$rootScope.items) {
    $rootScope.items = {};
  }
  return {
    load: function() {
      var res = $http.get("/public/javascripts/ng/mock/items.json");
      res.success(function(data) {
        $rootScope.items = {};
        $rootScope._items = data || [];
        for (var i = 0; i < data.length; i++) {
          var store = data[i].store;
          $rootScope.items[store] = $rootScope.items[store] || [];
          $rootScope.items[store].push(data[i]);
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

      console.log(results)
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

Game.factory('$plantation', ['$rootScope', '$http',
    function($rootScope, $http) {
      return {
        load: function() {
          if (!$rootScope.plantation) {
            var res = $http.get("/AuthController/plantation");
            res.success(function(data) {
              $rootScope.plantation = data;
            });
          }
        }
      };
    }]);

Game.factory('$diseases', ['$rootScope', '$http', function($rootScope, $http) {
  return {
    load: function() {
      var res = $http.get('/DeseasesExpertSystem/getDeseasePossibility');
      res.success(function(data) {
        $rootScope.diseases = data;
      });
    }
  };
}]);

Game.factory('$day', ['$rootScope', '$http', '$weather', '$diseases',
    function($rootScope, $http, $weather, $diseases) {
      return {
        next: function() {
          var res = $http.get("/application/nextday");
          res.success(function(data) {
            $rootScope.day = data;
            $weather.load();
            $diseases.load();
          });
        }
      };
    }]);
