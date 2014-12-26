Game.controller('BoughtItemsController', ['$scope', 'BoughtItems','State',
    function($scope, BoughtItems, State) {
	  $scope.boughtItems = [];
      State.subscribe('boughtItems','BoughtItemsController', function(data) {
    	  $scope.boughtItems = data;
      })
      $scope.executeItem = function(a) {
    	  alert(a);
      }
    }]);
