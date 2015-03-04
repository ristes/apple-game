Game.directive('smackInsectsDirective', ['$interval',function($interval) {
	return {
		restrict: 'E',
	    scope: {
	      gWidth: '@',
	      gHeight: '@'
	    },
	    templateUrl: '/public/_views/smack_insects_game.html',
	    link: function(scope, element, attrs) {
	    	scope.render = function() {
	    		
	    	}
	    }
	}
}]);