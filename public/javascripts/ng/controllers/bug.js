Game.controller("BugController", ["$scope","$http", function($scope, $http) {
	$scope.visible = false;
	$scope.isSubmitted = false;
	$scope.openDialog = function() {
		$scope.visible = true;
	}
	$scope.submit = function(bug) {
		$http.post("/BugReportController/save",bug).then(function(data) {
			if (data.data.status) {
				$scope.isSubmitted = true;
			}
		})
	}
	
	$scope.tryNew = function() {
		$scope.isSubmitted = false;
	}
}]);