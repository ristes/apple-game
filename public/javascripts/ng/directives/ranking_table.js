GameDirectives.directive('rankingTable', [
  function($) {
    return {
      restrict: 'E',
      scope: {
        visible: "=",
        years: "="
      },
      controller: function($scope, Ranking) {
    	  $scope.selectedYear = {id:$scope.$root.farmer.year_order, year: 2020 + $scope.$root.farmer.year_order};
    	  $scope.changeYear = function(selectedYear) {
    		   Ranking.ranks(selectedYear.year, function(data) {
    			   $scope.rankings = data;
    		  });
    	 }

    	  
      },
      templateUrl: '/public/templates/ranking-table.html',
      link: function(scope, element, attrs) {
        scope.displayedRankings = [].concat(scope.rankings);

        scope.hide = function(){
          scope.visible = false;
        }
      }
    };

  }
]);
