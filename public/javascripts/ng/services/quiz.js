Game.factory('Quiz', [ '$http', 'State', function($http, State) {
	return {
		load : function() {
			var res = $http.get("/QuestionnaireController/quiz");
			res.success(function(data) {
				State.set('quiz', data);
			});
		},

		submit : function(correct, wrong, callback) {
			var params = {};
			params.correct = correct;
			params.wrong = wrong;

			$http({
				method : 'POST',
				url : "/QuestionnaireController/submit",
				data : $.param(params),
				headers : {
					'Content-Type' : 'application/x-www-form-urlencoded'
				}
			}).success(function(data) {
				State.set("farmer", data.farmer);
				callback(data);
			})

		}
	}
} ]);