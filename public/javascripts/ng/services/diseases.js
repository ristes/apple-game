Game.factory('Diseases', ['$http', 'State','BubbleNotification', function($http, State, BubbleNotification) {

  return {
    load: function() {
      var res = $http.get('/DeseasesExpertSystem/getOccurredDiseases');
      res.success(function(data) {
        State.set('diseases', data);
        BubbleNotification.warning(data);
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