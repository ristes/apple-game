Game.controller('BuyBaseController', ['$scope', 'Planting','ExpertAdvice','$translate',
    function($scope, Planting, ExpertAdvice, $translate) {
		ExpertAdvice.setAdvice($translate("buy_rootstock.advice"));
		$scope.visible= true;
		$scope.storeUrl= '/public/images/game/stores/terrain.png';
		$scope.description = 'Base Store';
      $scope.bases = [];
      Planting.availableBases(function(data) {
        $scope.bases = data;
      });

      $scope.onHover = function(item) {
    	  ExpertAdvice.setAdvice(item.description);
      }
      
      $scope.buyBase = function(base) {
        Planting.buyBase(base);
      };

    }]);
