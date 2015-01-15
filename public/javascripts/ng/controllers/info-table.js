Game.controller('InfoTableController',['$scope','State', function($scope, State) {
	var onData = function(info) {
		for (var i = 0;i<info.length;i++) {
			$scope.$root.$emit('info-show', {
				infos : info,
				showNext : true,
				titleImageUrl : '/public/images/game/jabolko.png'
			});
		}
	}
	State.subscribe('info-table','InfoTableController',onData);
	
}]);