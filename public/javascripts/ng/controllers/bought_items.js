Game.controller('BoughtItemsController', ['$scope', 'BoughtItems','State','ExpertAdvice',
    function($scope, BoughtItems, State,ExpertAdvice) {
	  $scope.boughtItems = [];
      State.subscribe('boughtItems','BoughtItemsController', function(data) {
    	  $scope.boughtItems = data;
      })
      $scope.executeItem = function(a) {
    	  alert(a);
      }
      
      $scope.onHover = function(a) {
    	  ExpertAdvice.setAdvice(a.description);
      }
    }]);
