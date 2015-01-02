Game.factory('Quiz', ['$http','State', function($http,State) {
  return {
    load: function() {
      var res = $http.get("/QuestionnaireController/quiz");
      res.success(function(data) {
    	  State.set('quiz', data);
      });
    }
  }
}]);