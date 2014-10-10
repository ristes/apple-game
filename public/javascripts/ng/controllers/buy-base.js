Game.controller('BuyBaseController', ['$scope', 'Planting',
    function($scope, Planting) {

      $scope.bases = [];
      Planting.availableBases(function(data) {
        $scope.bases = data;
      });

      $scope.buyBase = function(base) {
        Planting.buyBase(base);
      };

    }]);
