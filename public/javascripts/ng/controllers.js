'use strict';

Game.controller('LoginController', ['$scope', '$translate',
    function($scope, $translate) {

    }
]);
Game.controller("DiseaseNotificationPanel", ["$scope","$timeout", function($scope,$timeout) {
	$scope.visible = false;
	$timeout(function() {
		$scope.visible = true;
	}, 2000);
}]);