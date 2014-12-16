Game.controller('TipsController', ['$scope', 'State', '$modal',
    '$timeout', function($scope, State, $modal, $timeout) {
      var windowTemplateUrl = '/public/templates/tip-dialog.html';

      $scope.subscribe = State.subscribe('status', 'TipsController', function(data) {
        $scope.message = data.tip;
        if (data.tip && data.tip!="") {
        	$scope.$root.tip = data.tip;
//        	var notification = $modal.open({
//        		backdrop: 'false',
//        		templateUrl: windowTemplateUrl,
//        		scope: $scope,
//        		windowClass :'tip-dialog'
//        	});
//        	$timeout(function() {
//        		notification.dismiss('cancel');
//        	}, 4000);
        } else {
        	//$scope.$root.tip = null;
        }
       });

    }]);