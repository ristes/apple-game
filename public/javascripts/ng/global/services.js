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
    },
    check: function(key) {
      return $rootScope.items[key] != null;
    },
    use: function(key) {
      $rootScope.items[key].pop();
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
          if(!$rootScope.plantation) {
            var res = $http.get("/AuthController/plantation");
            res.success(function(data) {
              $rootScope.plantation = data;
            });
          }
        }
      };
    }]);

Game.factory('$diseases',['$rootScope','$http',
                          function($rootScope, $http) {
                      		return {
                      			load:function() {
                      				var res = $http.get('/DeseasesExpertSystem/getDeseasePossibility');
                      				res.success(function(data) {
                      					$rootScope.diseases = data;
                      				});
                      			}
                      		};
                      } ]);

Game.factory('$day', ['$rootScope', '$http', '$weather','$diseases',
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


