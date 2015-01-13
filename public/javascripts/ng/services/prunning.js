Game.factory('Prunning', ['$http', 'State', function($http, State) {
  return {
    prune: function(gameData,callback) {
      var res = $http.get("/PrunningController/prune?goodPercent="+gameData.correct/gameData.total);
      res.success(function(data) {
        if (data.status === true) {
          State.set('farmer', data.farmer);
          if (typeof callback === 'function') {
            callback();
          }
        }
      });
    }
   
}}]);