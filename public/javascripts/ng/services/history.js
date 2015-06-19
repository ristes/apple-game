Game.factory('History', ['$http', 'State', function($http, State) {
  return {
    load: function(year,callback) {
      $http.get("/ActionsPreviewController/log?year="+year+"&typeLog=8").then(function(result){
        callback(result.data);
      });
    }

  };
}]);