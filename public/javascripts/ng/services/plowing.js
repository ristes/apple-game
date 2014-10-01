Game.factory('Plowing', ['$http', 'State', function($http, State) {
  return {
    plowing: function(callback) {
      var res = $http.get("/LandTreatmanController/plowing");
      res.success(function(data) {
        if (data.status === true) {
          State.set('farmer', data.farmer);
          if (typeof callback === 'function') {
            callback();
          }
        }
      });
    },
    digging: function(callback) {
      var res = $http.get("/LandTreatmanController/digging");
      res.success(function(data) {
        if (data.status === true) {
          State.set('farmer', data.farmer);
          if (typeof callback === 'function') {
            callback();
          }
        }
      });
    },
    deepPlowing: function(calback) {
      var res = $http.get("/LandTreatmanController/plowing");
      res.success(function(data) {
        var res = $http.get("/LandTreatmanController/digging");
        res.success(function(data) {
          if (data.status === true) {
            State.set('farmer', data.farmer);
            if (typeof callback === 'function') {
              callback();
            }
          }
        });
      });
    }
  };
}]);