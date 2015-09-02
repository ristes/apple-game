GameDirectives.directive('customAudio', [
		'ngAudio',
		'audioManager',
		function(ngAudio, audioManager) {
			return {
				transclude : true,
				restrict : 'E',
				scope : {
					filename : '=',
					startOnInit : '='
				},
				link : function($scope) {
					$scope.$on("$destroy", function() {
						$scope.audio.stop();
					});
				},
				templateUrl : '/public/templates/customAudio.html',
				controller : function($scope) {
//					audioManager.subscribe(function(filename) {
//						$scope.audio.stop();
//						if (filename !== undefined && filename !== null) {
//							$scope.audio = ngAudio.load(filename);
//							$scope.audio.loop = true;
//							$scope.audio.volume = 0.5;
//							if ($scope.startOnInit
//									&& $scope.$root.soundsEnabled) {
//								$scope.audio.play();
//							}
//						}
//					})
					$scope.audio = ngAudio.load($scope.filename);
					$scope.audio.loop = true;
					$scope.audio.volume = 0.5;
					$scope.disablePreload = true;
					// $scope.audio.play();
					if ($scope.startOnInit) {
						$scope.audio.play();
					}
					
					$scope.$on("$destroy", function() {
						$scope.audio.stop();
						$scope.audio.unbind();
					})

				}
			};
		} ]);