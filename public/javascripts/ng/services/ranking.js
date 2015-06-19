Game.factory('Ranking', ['$http', 'State', function($http, State) {
  return {
    ranks: function(selectedYear,callback) {
      return $http.get("rankingcontroller/rank?year="+selectedYear).then(function(result){
        callback(result.data);
      });
    },

  };
}]);