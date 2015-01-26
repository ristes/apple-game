Game.factory('Resume', ['$http','State', function($http,State) {
  return {
    load: function() {
      var res = $http.get("/ResumeController/resume");
      res.success(function(data) {
    	  var result = {'data': data};
    	  State.set('resume', result);
      });
    },
    setSeen: function() {
    	var res = $http.get("/ResumeController/seenResume");
    	res.success(function(data) {
    		
    	});
    }
  }
}]);