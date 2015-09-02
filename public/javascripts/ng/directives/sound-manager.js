Game.directive("soundManager",["ngAudio","audioManager", function(ngAudio,audioManager) {
	return {
		restrict:'E',
		templateUrl : '/public/templates/sound-manager.html',
		controller: function($scope) {
			$scope.$root.soundsEnabled  = true;
			$scope.isMuted = false;
			$scope.mute=function() {
				$scope.isMuted = true;
				$scope.$root.soundsEnabled  = false;
				audioManager.notify(undefined);
				$scope.$root.$emit("sound-stop");
			},
			$scope.unmute=function() {
				$scope.isMuted = false;
				$scope.$root.soundsEnabled  = true;
				$scope.$root.$emit("sound-start");
			}
		}
	}
}]);
