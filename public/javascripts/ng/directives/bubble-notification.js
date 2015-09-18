GameDirectives.directive("bubbleNotification", ["BubbleNotification", function( BubbleNotification) {
	return {
		transclude : true,
		restrict : 'E',
		
		link: function($scope) {
			
		},
		templateUrl : '/public/templates/disease-bubble-notification.html',
		controller: function($scope) {
			$scope.diseases = [];
			
			BubbleNotification.subscribe("DiseasesNotification", function(diseases) {
				$scope.diseases = _.sortBy(diseases, function(disease) {
					return disease;
				});
			});
			$scope.showDiseases = function() {
				$scope.$root.$emit("operation-desease-analysis");
			}
		}
			
		
			
			
	}
}]);