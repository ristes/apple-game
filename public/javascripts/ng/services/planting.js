Game.factory('Planting', ['$http', 'State', function($http, State) {

  function doGet(url, callback) {
    var res = $http.get(url);
    res.success(function(data) {
      if (typeof callback === 'function') {
        callback(data);
      }
    });
  }

  return {
    seedlings: function(callback) {
      doGet("/PlantationController/seedlings", callback);
    },
    plantTypes: function(callback) {
      doGet("/PlantationController/plantTypes", callback);
    },
    seedlingTypes: function(callback) {
      doGet("/PlantationController/seedlingTypes", callback);
    },
    analyseTerain: function(id, callback) {
      doGet("/PlantationController/soilanalyse", function(data) {
        State.set("terrainAnalyse", data);
        callback(data);
      });
    },
    buySeedlings: function(seedlings, callback) {
      var url = "/PlantationController/buySeedlings";
      var params = {
        nextState: '/buy_base'
      };
      for (var i = 0; i < seedlings.length; i++) {
        params['s' + i] = seedlings[i];
        params['q' + i] = seedlings[i].quantity;
      }
      var res = $http({
        method: "POST",
        url: url,
        data: $.param(params),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      res.success(function(data) {
        if (typeof callback === 'function') {
          callback(data);
        }
      });

    },
    availableBases: function(callback) {
      doGet("/PlantationController/availableBases", callback);
    },
    availableSeedlings: function(callback) {
      doGet("/PlantationController/availableSeedlings", callback);
    },
    buyBase: function(base, callback) {
      var url = "/PlantationController/buyBase";
      var params = {
        base: base,
        nextState: '/plantation'
      };
      var res = $http({
        method: "POST",
        url: url,
        data: $.param(params),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      });
      res.success(function(data) {
        if (typeof callback === 'function') {
          callback(data);
        }
      });
    }
  };

}]);
