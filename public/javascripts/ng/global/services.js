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
            var res = $http.get("/public/javascripts/ng/mock/farmer.json");
            // var res = $http.get("/AuthController/farmer");
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
          $rootScope.items[store].push(data);
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
    use: function(key) {
      $rootScope.items[key].pop();
      var results = [];
      var removed = false;
      angular.forEach($rootScope._items, function(val) {
        if (val.store == key && !removed) {
          removed = true;
        } else {
          results.push(val);
        }
      });
      $rootScope._items = results;
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

Game.factory('$day', ['$rootScope', '$http', '$weather',
    function($rootScope, $http, $weather) {
      return {
        next: function() {
          var res = $http.get("/global/day/next");
          res.success(function(data) {
            $rootScope.day = data;
            $weather.load();
          });
        }
      };
    }]);
