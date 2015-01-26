Game.controller('TipsController', ['$scope', 'State', '$modal',
    '$timeout', function($scope, State, $modal, $timeout) {
      var windowTemplateUrl = '/public/templates/tip-dialog.html';
      
      $scope.priorities = {'':0,'tip':1,'disease':2};
      $scope.current_data = "";
      
      $scope.isHigherPriority = function(priority) {
    	  if ($scope.priorities[$scope.current_data]<$scope.priorities[priority]) {
    		  return true;
    	  }
    	  return false;
      };

      $scope.subscribe = State.subscribe('status', 'TipsController', function(data) {
        $scope.message = data.tip;
        if (data.tip && data.tip!="") {
        	if ($scope.isHigherPriority('tip')) {
        		$scope.$root.tip = data.tip;
        		$scope.$root.type_alert = 'info';
        	}
        }
        if (data.farmer.hasNewDisease) {
        	if ($scope.isHigherPriority('disease')) {
        		 $scope.$root.tip = 'You have a disease on your field. Please prevent...';
        		 $scope.$root.type_alert = 'danger';
        	}
        }
       });
      

      
      $scope.isInfo = function() {
    	  return $scope.$root.type_alert === 'info';
      }
      
      $scope.isDanger = function() {
    	  return $scope.$root.type_alert === 'danger';
      }
      
    }]);