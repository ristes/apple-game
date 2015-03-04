Game.factory('Prunning', ['$http', 'State', function($http, State) {
  return {
	checkPrune: function(yes,no) {
		 var res = $http.get("/PrunningController/checkprunning");
		 res.success(function(data) {
			 debugger;
			 if (data.status==true) {
				 yes()
			 } else {
				 no(data.message);
			 }
		 });
	},
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