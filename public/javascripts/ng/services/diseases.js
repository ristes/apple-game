Game.factory('Diseases', ['$http', 'State', function($http, State) {

  return {
    load: function() {
      var res = $http.get('/DeseasesExpertSystem/getOccurredDiseases');
      res.success(function(data) {
        State.set('diseases', data);
      });
    },
    getHintAsync: function(name, callback) {
      var res = $http.get('/DeseasesExpertSystem/buyAdvice?name=' + name);
      res.success(function(data) {
        if (data.status) {
          State.set('farmer', data.farmer);
          if (typeof callback === 'function') {
            callback(data.hint);
          }
        }
      });
    },
    get: function() {
      return State.getByField('diseases');
    }
  };
}]);