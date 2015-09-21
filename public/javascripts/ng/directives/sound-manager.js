Game.directive("soundManager",["ngAudio","audioManager","$http","$day","State", function(ngAudio,audioManager, $http, $day, State) {
	return {
		restrict:'E',
		templateUrl : '/public/templates/sound-manager.html',
		controller: function($scope) {
			$scope.isMuted = false;
			$scope.mute=function() {
				$http.get("http://localhost:9001/SettingsController/sounds?status=false").success(function(data) {
					if (data.status===true) {
						State.set("farmer",data.t);
						audioManager.notify(undefined);
						$scope.$root.$emit("sound-stop");
					}
				});
				$scope.$root.isMuted = true;
				
			},
			$scope.unmute=function() {
				$scope.isMuted = false;
				
				$http.get("http://localhost:9001/SettingsController/sounds?status=true").success(function(data) {
					if (data.status===true) {
						State.set("farmer",data.t);
						audioManager.notify(undefined);
						$scope.$root.$emit("sound-start");
					}
				});
				
//				$scope.$day.soundsEnabled  = true;
//				$scope.$root.$emit("sound-start");
			}
		}
	}
}]);
