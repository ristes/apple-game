Game.controller('HistoryController', ['$scope','$translate', 'History','$day', function($scope, $translate, History, $day) {
  $scope.visible = false;

  $scope.showHistory = function(){
    $scope.visible = true;
    $scope.getHistory();

  }

  $scope.getHistory = function(){
	  var farmer = $day.get();
	  var year = farmer.year_order+2020;
	  History.load(year, function(data) {
		  $scope.history = data;
	  });
  }

  $scope.getHistory();

}]);