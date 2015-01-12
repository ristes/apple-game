Game.factory('Ranking', ['$http', 'State', function($http, State) {
  return {
    ranks: function(deep,callback) {
      return $http.get("/Application/rankings").then(function(result){
        return result.data;
      });
    },

  };
}]);