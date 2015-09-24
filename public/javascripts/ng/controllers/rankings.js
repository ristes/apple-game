Game.controller('RankingsController', ['$scope', 'Ranking', function($scope, Ranking) {
  $scope.visible = false;

  $scope.showRankings = function(){
    $scope.visible = true;
    $scope.getRankings();
  }
  var years = new Array();
  for (var i=0;i<30;i++) {
	  years.push({
		  id: i,
		  year: parseInt(2020)+parseInt(i)
	  });
  }
  $scope.selectedYear = 2020 + $scope.$root.farmer.year_order;
  $scope.years = years;

 
//  $scope.getRankings();

}]);