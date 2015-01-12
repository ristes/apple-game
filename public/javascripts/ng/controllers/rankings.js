Game.controller('RankingsController', ['$scope', 'Ranking', function($scope, Ranking) {
  $scope.visible = false;

  $scope.showRankings = function(){
    $scope.visible = true;
    $scope.getRankings();

  }

  $scope.getRankings = function(){
    Ranking.ranks().then(function(rankings){
      $scope.rankings = rankings;
    });
  }

  $scope.getRankings();

}]);