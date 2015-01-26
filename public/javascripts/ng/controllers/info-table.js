Game.controller('InfoTableController',['$scope','State', function($scope, State) {
//	$scope.visible = false;
	var onData = function(info) {
//		$scope.visible=true;
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