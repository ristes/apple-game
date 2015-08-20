Game.directive("soundManager",["ngAudio","audioManager", function(ngAudio,audioManager) {
	return {
		restrict:'E',
		templateUrl : '/public/templates/sound-manager.html',
		controller: function($scope) {
			$scope.isMuted = false;
			$scope.mute=function() {
				$scope.isMuted = true;
				$scope.$root.$emit("sound-stop");
			},
			$scope.unmute=function() {
				$scope.isMuted = false;
				ngAudio.unmute();
				$scope.$root.$emit("sound-start");
			}
		}
	}
}]);
