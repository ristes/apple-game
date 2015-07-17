Game.directive("soundManager",["ngAudio","audioManager", function(ngAudio,audioManager) {
	return {
		restrict:'E',
		templateUrl : '/public/templates/sound-manager.html',
		controller: function($scope) {
			$scope.mute=function() {
				console.log("mute");
				ngAudio.mute();
			},
			$scope.unmute=function() {
				console.log("unmute");
				ngAudio.unmute();
			}
		}
	}
}]);
