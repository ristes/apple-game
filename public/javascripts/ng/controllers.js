'use strict';

Game.controller('LoginController', ['$scope', '$translate',
    function($scope, $translate) {

    }
]);
Game.controller("DiseaseNotificationPanel", ["$scope","$timeout",'BubbleNotification', function($scope,$timeout, BubbleNotification) {
	$scope.visible = false;
	$timeout(function() {
		$scope.visible = true;
	}, 2000);
}]);