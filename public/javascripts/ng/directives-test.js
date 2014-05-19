'use strict';

/* Directives */

angular.module('Game.directives', ['ngResource', 'ngRoute', 'ngAnimate', 'ngCookies', 'ui.bootstrap', 'toaster', 'pascalprecht.translate']).directive('appVersion', ['version',
function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
}]).directive('testDialog', function() {
	return {
		restrict : 'E',
		transclude : true,
		scope : {
			diseases : '=',
			report:'='
		},
		link : function(scope, element, attrs, ctrl, transclude, formCtrl) { 
			
		},
		templateUrl : '/public/templates/test-dialog.html'
	};
}); 