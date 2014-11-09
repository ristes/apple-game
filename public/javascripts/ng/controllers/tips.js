Game.controller('TipsController', ['$scope', 'State', '$modal',
    '$timeout', function($scope, State, $modal, $timeout) {
      var windowTemplateUrl = '/public/templates/tip-dialog.html';

      State.subscribe('status', 'TipsController', function(data) {
        $scope.message = data.tip;
        if (data.tip && data.tip!="") {
        	var notification = $modal.open({
        		backdrop: 'false',
        		templateUrl: windowTemplateUrl,
        		scope: $scope
        	});
        	$timeout(function() {
        		notification.dismiss('cancel');
        	}, 5000);
        }
       });

    }]);